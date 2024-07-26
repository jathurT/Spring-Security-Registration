package com.jaka.springsecurityregistration.Event;


import com.jaka.springsecurityregistration.Entity.User;
import com.jaka.springsecurityregistration.Entity.VerificationToken;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter
public class ResendVerificationTokenCompleteEvent extends ApplicationEvent {
  private User user;
  private String applicationURL;
  private VerificationToken verificationToken;
  private Locale locale;

  public ResendVerificationTokenCompleteEvent(User user, String applicationURL, VerificationToken verificationToken,Locale locale) {
    super(user);
    this.user = user;
    this.applicationURL = applicationURL;
    this.verificationToken = verificationToken;
    this.locale = locale;
  }
}
