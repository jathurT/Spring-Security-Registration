package com.jaka.SpringRegistration.Event;

import com.jaka.SpringRegistration.Entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Setter
@Getter
public class RegistrationCompleteEvent extends ApplicationEvent {
  private User user;
  private String applicationURL;
  private Locale locale;


  public RegistrationCompleteEvent(User user, String applicationURL, Locale locale) {
    super(user);
    this.user = user;
    this.applicationURL = applicationURL;
    this.locale = locale;
  }
}
