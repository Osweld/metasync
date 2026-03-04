package com.osweld.metasync.shared.email.infrastructure.resend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.osweld.metasync.shared.email.domain.model.EmailMessage;
import com.osweld.metasync.shared.email.domain.port.EmailSender;
import com.osweld.metasync.shared.email.domain.port.TemplateEngine;
import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResendEmailSender implements EmailSender {

    private final TemplateEngine templateEngine;
    private final Resend resend;

    @Value("${resend.from}")
    private String from;

    @Override
    public void send(EmailMessage message) {
        log.info("Attempting to send email to {} with template: {}", 
                 message.recipient(), message.templateName());
        
        try {
            String htmlContent = templateEngine.render(
                    message.templateName(), 
                    message.templateModel());

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(from)
                    .to(message.recipient())
                    .subject(message.subject())
                    .html(htmlContent)
                    .build();

            resend.emails().send(params);
            
            log.info("Email successfully sent to {} with subject: {}", 
                     message.recipient(), message.subject());
                     
        } catch (Exception e) {
            log.error("Error sending email to {} with template {}: {}", 
                      message.recipient(), 
                      message.templateName(), 
                      e.getMessage(), 
                      e);
            throw new RuntimeException("Error sending email", e);
        }
    }
}
