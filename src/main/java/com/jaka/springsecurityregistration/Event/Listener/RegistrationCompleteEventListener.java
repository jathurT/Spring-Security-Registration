package com.jaka.springsecurityregistration.Event.Listener;


import com.jaka.springsecurityregistration.Entity.User;
import com.jaka.springsecurityregistration.Event.RegistrationCompleteEvent;
import com.jaka.springsecurityregistration.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

  @Autowired
  private UserService userService;

  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private MessageSource messageSource;

  @Override
  public void onApplicationEvent(RegistrationCompleteEvent event) {
    User user = event.getUser();
    String token = UUID.randomUUID().toString();
    userService.saveVerificationTokenForUser(token, user);

    String confirmationUrl = event.getApplicationURL() + "/verifyRegistration?token=" + token;
    String resendConfirmationUrl = event.getApplicationURL() + "/resendVerifyToken?token=" + token;
//    log.info("Click the link to verify your account: {}", confirmationUrl);

    // Sending Mail to user
    String recipientAddress = user.getEmail();
    String subject = "Registration Confirmation";
    String message = messageSource.getMessage("message.regSucc", null, event.getLocale());
    String resendVerificationTokenMessage= messageSource.getMessage("message.resVerTokMessa",null,event.getLocale());
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setText(message + "\r\n" + confirmationUrl + "\r\n" +
      resendVerificationTokenMessage + "\r\n" + resendConfirmationUrl);
    simpleMailMessage.setTo(recipientAddress);
    mailSender.send(simpleMailMessage);
    log.info("Mail has been sent successfully" + " url: " + confirmationUrl);
  }
}
