package org.redex.backend.zelper.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailContentBuilder {

    @Autowired
    private TemplateEngine templateEngine;

    public String build(MailEnum mail, Context ctx) {
        return templateEngine.process(mail.getTemplate(), ctx);
    }
}
