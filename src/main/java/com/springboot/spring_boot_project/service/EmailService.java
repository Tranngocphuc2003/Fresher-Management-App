package com.springboot.spring_boot_project.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;
import java.util.Map;
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Service
public class EmailService {
    private JavaMailSender javaMailSender;
    private SpringTemplateEngine templateEngine;
    public void sendMail(String to, String subject, Map<String, Object> model, String templateName){
        Context context = new Context();
        context.setVariables(model);

        String htmlContent = templateEngine.process(templateName,context);

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);  // Set true for HTML content
            javaMailSender.send(message);

            System.out.println("Mail sent successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        }
    }

