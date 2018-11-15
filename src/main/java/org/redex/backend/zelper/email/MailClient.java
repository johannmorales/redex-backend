package org.redex.backend.zelper.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class MailClient {

    @Autowired
    MailContentBuilder mailContentBuilder;

    @Autowired
    private JavaMailSender mailSender;

    public void prepareAndSend(String recipient, String subject, MailEnum mail, Context context) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("redex@redex.org");
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);
            String content = mailContentBuilder.build(mail, context);
            messageHelper.setText(content, true);
        };
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
        }
    }

    public void prepareAndSend(String recipient, MailEnum mail, Context context) {
        prepareAndSend(recipient, mail.getDefaultSubject(), mail, context);
    }


}