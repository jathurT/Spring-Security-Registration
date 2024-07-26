package com.jaka.springsecurityregistration.Event;

import com.jaka.springsecurityregistration.Entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter
public class ResetPasswordCompleteEvent extends ApplicationEvent {
  private User user;
  private String url;
  private String token;
  private Locale locale;

  public ResetPasswordCompleteEvent(User user, String url, String token, Locale locale) {
    super(user);
    this.user = user;
    this.url = url;
    this.token = token;
    this.locale = locale;
  }
}
