package com.nykniu.dodoo;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class PdfHandler {

    public static void main(String[] args) throws IOException, TesseractException {
        byte[] pdfBytes = Files.readAllBytes(new File("C:\\Users\\Laxelott\\AppData\\Local\\Microsoft\\Windows\\INetCache\\IE\\TVUFOIQX\\cif-PAAC9801042J3_hIvOskvwzG[1].pdf").toPath());;
        HashMap result = extractData(pdfBytes);
        System.out.println(result.toString());
    }

    public static HashMap<String, String> extractData(byte[] fileBytes) throws IOException, TesseractException {
        HashMap<String, String> result = new HashMap<>();

        result.put("rfc", getText(900, 1340, 1200, 70, fileBytes));
        result.put("name", getText(900, 1520, 1200, 70, fileBytes));
        result.put("pSurname", getText(900, 1610, 1200, 70, fileBytes));
        result.put("mSurname", getText(900, 1700, 1200, 70, fileBytes));
        result.put("street", getText(100, 2390, 1100, 70, fileBytes).split(":")[1]);
        result.put("intNum", getText(100, 2480, 1100, 70, fileBytes).split(":")[1]);
        result.put("extNum", getText(1300, 2390, 1100, 70, fileBytes).split(":")[1]);
        result.put("suburb", getText(1300, 2480, 1100, 70, fileBytes).split(":")[1]);
        result.put("town", getText(1300, 2570, 1100, 70, fileBytes).split(":")[1]);
        result.put("postalCode", getText(100, 2300, 1100, 70, fileBytes).split(":")[1]);

        return result;
    }

    public static String getText(int x, int y, int w, int h, byte[] fileBytes) throws IOException, TesseractException {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(fileBytes))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage fullImage = pdfRenderer.renderImageWithDPI(0, 300);

            Rectangle area = new Rectangle(x, y, w, h);
            BufferedImage croppedImage = fullImage.getSubimage(area.x, area.y, area.width, area.height);

            // ImageIO.write(croppedImage, "png", new File("cropped.png")); // Debugging

            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("./rsc/tessdata");
            tesseract.setLanguage("spa");

            return tesseract.doOCR(croppedImage);
        }
    }
}
