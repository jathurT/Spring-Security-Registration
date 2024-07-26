package com.jaka.SpringRegistration.Event.Listener;

import com.jaka.SpringRegistration.Event.ResendVerificationTokenCompleteEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResendVerificationTokenCompleteEventListener implements ApplicationListener<ResendVerificationTokenCompleteEvent> {
  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private MessageSource messageSource;

  @Override
  public void onApplicationEvent(ResendVerificationTokenCompleteEvent event) {
    String confirmationUrl = event.getApplicationURL() + "/verifyRegistration?token=" + event.getVerificationToken().getToken();
    log.info("Click the link to verify your account: {}", confirmationUrl);

    String recipientAddress = event.getUser().getEmail();
    String subject = "Registration Confirmation";
    String message = messageSource.getMessage("message.regSucc", null, event.getLocale());
    String resendVerificationTokenMessage= messageSource.getMessage("message.resVerTokMessa",null,event.getLocale());

    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setSubject(subject);
    String resendConfirmationUrl = event.getApplicationURL() + "/resendVerifyToken?token=" + event.getVerificationToken().getToken();;
    simpleMailMessage.setText(message + "\r\n" + confirmationUrl + "\r\n" +
      resendVerificationTokenMessage + "\r\n" + resendConfirmationUrl);
    simpleMailMessage.setTo(recipientAddress);
    mailSender.send(simpleMailMessage);
    log.info("Mail has been sent successfully" + " url: " + confirmationUrl);
  }
}
