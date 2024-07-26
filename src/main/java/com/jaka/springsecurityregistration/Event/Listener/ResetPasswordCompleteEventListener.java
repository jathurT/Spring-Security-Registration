package com.jaka.SpringRegistration.Event.Listener;

import com.jaka.SpringRegistration.Event.ResetPasswordCompleteEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResetPasswordCompleteEventListener implements ApplicationListener<ResetPasswordCompleteEvent> {
  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  private MessageSource messageSource;

  @Override
  public void onApplicationEvent(ResetPasswordCompleteEvent event) {
    String resetPasswordUrl = event.getUrl() + "/savePassword?token=" + event.getToken();
    String recipientMail = event.getUser().getEmail();
    String subject = "Password Reset Confirmation";
    String message = messageSource.getMessage("message.resetPasswordMessage", null, event.getLocale());
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(recipientMail);
    mailMessage.setText(message + "\r\n" + resetPasswordUrl);
    mailMessage.setSubject(subject);
    javaMailSender.send(mailMessage);
    log.info("Mail for reset the password has been sent successfully to " + recipientMail);
  }
}
