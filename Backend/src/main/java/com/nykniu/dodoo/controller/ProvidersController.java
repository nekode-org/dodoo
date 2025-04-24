package com.nykniu.dodoo.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import javax.mail.MessagingException;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.yaml.snakeyaml.Yaml;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nykniu.dodoo.GlobalVars;
import com.nykniu.dodoo.PdfHandler;
import com.nykniu.dodoo.manager.MailManager;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sourceforge.tess4j.TesseractException;

@RequestMapping("/providers")
public class ProvidersController {

    /**
     * Obtener información del refc y de los tickets según los proveedores. Se
     * ignora el rfc en texto si es que hay un archivo pdf
     * 
     * @param rfc          RFC del contribuyente en forma de String
     * @param rfcPdf       RCF del contribuyente en forma de pdf en base 64
     * @param ticketsArray Arreglo de los folios a obtener
     * @return Una lista de los folios con la información obtenida y la información
     *         del contribuyente
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws FileNotFoundException
     */
    @RequestMapping(value = "/getInfo", method = RequestMethod.POST, produces = "application/json")
    public String getInfo(
            ModelMap model, HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "rfc", required = true) String rfc,
            @RequestParam(value = "rfcPdf", required = false) String rfcPdf,
            @RequestParam(value = "tickets", required = true) JsonArray ticketsArray)
            throws ClassNotFoundException, SQLException, FileNotFoundException {

        HashMap<String, String> rfcData = null;
        int errorCode = 0;
        if (rfcPdf != null) {
            byte[] pdfBytes = Base64.getDecoder().decode(rfcPdf);

            try {
                rfcData = PdfHandler.extractData(pdfBytes);
            } catch (IOException | TesseractException e) {
                errorCode = 1;
                System.err.println("OCR Error!");
                System.err.println(e.getLocalizedMessage());
            }
        } else {
            // TODO: Get RCF from own DB
        }

        JsonArray ticketResults = new JsonArray();
        for (int i = 0; i < ticketsArray.size(); i++) {
            ticketResults.add(
                    GlobalVars.connectionManager.getData(
                            ticketsArray.get(i).getAsJsonObject().get("providerCode").getAsInt(),
                            ticketsArray.get(i).getAsJsonObject().get("ticketCode").getAsString()));
        }

        JsonObject result = new JsonObject();
        result.add("ticketResults", ticketResults);
        result.addProperty("rfc", rfcData.get("rfc"));
        result.addProperty("country", "Mexico");
        result.addProperty("fullName",
                rfcData.get("name") + " " + rfcData.get("pSurname") + " " + rfcData.get("mSurname"));
        result.addProperty("street", rfcData.get("street"));
        result.addProperty("extNum", rfcData.get("extNum"));
        result.addProperty("intNum", rfcData.get("intNum"));
        result.addProperty("suburb", rfcData.get("suburb"));
        result.addProperty("town", rfcData.get("town"));
        result.addProperty("postalCode", rfcData.get("postalCode"));
        result.addProperty("error", errorCode);

        return result.toString();
    }

    /**
     * Usuario envía confirmación de información, con la cual se generan los
     * archivos de facturación y se envían en el correo de confirmación
     * 
     * @param email
     * @param confirmedData
     * @param ticketsArray
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException 
     * @throws MessagingException 
     */
    @RequestMapping(value = "/confirmInfo", method = RequestMethod.POST, produces = "application/json")
    public int getInfo(
            ModelMap model, HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "confirmedData", required = true) JsonObject confirmedData,
            @RequestParam(value = "tickets", required = true) JsonArray ticketsArray)
            throws ClassNotFoundException, SQLException, IOException, MessagingException {

        String templateHTML;
        HashMap<String, byte[]> attachments = new HashMap<>();

        for (int i = 0; i < ticketsArray.size(); i++) {
            JsonObject ticket = ticketsArray.get(i).getAsJsonObject();
            templateHTML = Files.readString(Paths.get("./rsc/templates/invoiceTemplate.html"));
            templateHTML
                    .replace("${rfc}", confirmedData.get("rfc").getAsString())
                    .replace("${fullName}", confirmedData.get("fullName").getAsString())
                    .replace("${street}", confirmedData.get("street").getAsString())
                    .replace("${extNum}", confirmedData.get("extNum").getAsString())
                    .replace("${intNum}", confirmedData.get("intNum").getAsString())
                    .replace("${state}", confirmedData.get("state").getAsString())
                    .replace("${town}", confirmedData.get("town").getAsString())
                    .replace("${suburb}", confirmedData.get("suburb").getAsString())
                    .replace("${postalCode}", confirmedData.get("postalCode").getAsString())
                    .replace("${ticketIssuer}", ticket.get("ticketIssuer").getAsString())
                    .replace("${ticketCode}", ticket.get("ticketCode").getAsString())
                    .replace("${ticketDate}", ticket.get("ticketDate").getAsString())
                    .replace("${ticketAmmount}", ticket.get("ticketAmmount").getAsString());
    
            PdfRendererBuilder builder = new PdfRendererBuilder();
            OutputStream os = new ByteArrayOutputStream();
            builder.useFastMode();
            builder.withHtmlContent(templateHTML.toString(), "./");
            builder.toStream(os);
            builder.run();
            
            byte[] pdfFile = ((ByteArrayOutputStream)os).toByteArray();
            attachments.put("Reporte PDF", pdfFile);
            // TODO Generar el archivo xml desde archivo xsd
            // attachments.put("Reporte XML", xmlFile);   
        }
        

        MailManager.sendEmail(email, "Reporte de Facturación", "Aquí está la factura que facturo con el sistema de facturación de facturaGFA", attachments);

        // TODO Guardar archivo xml en carpeta xml y guardar ruta en la bd

        return 0;
    }

    /**
     * Obtiene la lista de proveedores registrados
     * 
     * @return Lista de proveedores
     * @throws FileNotFoundException
     */
    @RequestMapping(value = "/getList", method = RequestMethod.GET, produces = "application/json")
    public String getList(
            ModelMap model, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        return GlobalVars.connectionManager.getProvidersList().toString();
    }
}
