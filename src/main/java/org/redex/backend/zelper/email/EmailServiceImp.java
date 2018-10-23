package org.redex.backend.zelper.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImp implements EmailService {

    @Autowired
    public JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Bean
    public SimpleMailMessage templatePasswordReestablecido() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("Su contrasena ha sido reestablecida: \n%s\n");
        return message;
    }

    @Bean
    public SimpleMailMessage templateNuevoUsuario() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("Su contrasena ha sido reestablecida: \n%s\n");
        return message;
    }
}
