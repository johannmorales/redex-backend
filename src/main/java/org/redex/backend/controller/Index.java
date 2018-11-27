package org.redex.backend.controller;

import org.redex.backend.zelper.email.MailClient;
import org.redex.backend.zelper.email.MailEnum;
import org.redex.backend.zelper.pdf.PdfClient;
import org.redex.backend.zelper.pdf.PdfEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

@RestController
public class Index {

    @Autowired
    PdfClient pdfClient;

    @Autowired
    MailClient mailClient;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String index() {
        return "RedEx - Backend";
    }
}

