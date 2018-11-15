package org.redex.backend.zelper.pdf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.FileSystems;

@Service
public class PdfClient {

    @Autowired
    PdfContentBuilder pdfContentBuilder;

    private static final String UTF_8 = "UTF-8";

    private String convertToXhtml(String html) throws UnsupportedEncodingException {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding(UTF_8);
        tidy.setOutputEncoding(UTF_8);
        tidy.setXHTML(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        return outputStream.toString(UTF_8);
    }

    public String generate(PdfEnum pdf, Context context) {
        try {
            String html = pdfContentBuilder.build(pdf, context);
            String xHtml = convertToXhtml(html);
            ITextRenderer renderer = new ITextRenderer();
            String baseUrl = FileSystems
                    .getDefault()
                    .getPath("src", "main", "resources", "pdf")
                    .toUri()
                    .toURL()
                    .toString();
            System.out.println(baseUrl);
            System.out.println(baseUrl);

            System.out.println(baseUrl);

            System.out.println(baseUrl);
            System.out.println(baseUrl);
            System.out.println(baseUrl);

            renderer.setDocumentFromString(xHtml, baseUrl);
            renderer.layout();
            OutputStream outputStream = new FileOutputStream("salida.pdf");
            renderer.createPDF(outputStream);
            outputStream.close();

            return "salida.pdf";

        } catch (Exception ex) {
            return null;
        }
    }
}
