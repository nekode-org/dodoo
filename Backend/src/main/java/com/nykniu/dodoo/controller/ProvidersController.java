package com.nykniu.dodoo.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nykniu.dodoo.GlobalVars;
import com.nykniu.dodoo.PdfHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sourceforge.tess4j.TesseractException;

@RequestMapping("/providers")
public class ProvidersController {

    /**
     * Obtener información del refc y de los tickets según los proveedores. Se
     * ignora el rfc en texto si es que hay un archivo pdf
     * 
     * @param email        Correo a dónde enviar el comprobante
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
            @RequestParam(value = "email", required = true) String email,
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
        result.addProperty("razon",
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
     * @param model
     * @param request
     * @param response
     * @param email
     * @param confirmedData
     * @param ticketsArray
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws FileNotFoundException
     */
    @RequestMapping(value = "/confirmInfo", method = RequestMethod.POST, produces = "application/json")
    public int getInfo(
            ModelMap model, HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "confirmedData", required = true) JsonObject confirmedData,
            @RequestParam(value = "tickets", required = true) JsonArray ticketsArray)
            throws ClassNotFoundException, SQLException, FileNotFoundException {

        // TODO Paso 2:
        //
        // TODO Generar el archivo xml desde archivo xsd
        // TODO Generar el archivo pdf
        // TODO Enviar un email de confirmación junto con los archivos
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
