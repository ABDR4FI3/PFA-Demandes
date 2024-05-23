package org.example.springpfa.Services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class GeneratePDF {
    public byte[] generatePdf() throws IOException {
        // Create a new document
        PDDocument document = new PDDocument();

        // Add a page
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        // Create a content stream for drawing
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Begin text and write content
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Confirmation ");
        contentStream.endText();

        // Close the content stream
        contentStream.close();

        // Save the document to a byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        document.close();

        return byteArrayOutputStream.toByteArray();
    }
}
