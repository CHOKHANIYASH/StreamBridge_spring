package com.chokhaniyash.streambridge.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@AllArgsConstructor
@Component
public class EmailUtil {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendSuccessMail(String reciever,String subject, Map<String,Object> model){
        try {
            sendEmail(reciever,subject,model,"transcodingSuccessEmail");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendFailedMail(String reciever,String subject, Map<String,Object> model){
        try {
            sendEmail(reciever,subject,model,"transcodingFailedEmail");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmail(String reciever, String subject, Map<String,Object> model,String template) throws MessagingException {
        Context context = new Context();
        context.setVariables(model);

        String htmlContent = templateEngine.process(template,context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(reciever);
        helper.setSubject(subject);
        helper.setText(htmlContent,true);

        mailSender.send(message);
    }

}
