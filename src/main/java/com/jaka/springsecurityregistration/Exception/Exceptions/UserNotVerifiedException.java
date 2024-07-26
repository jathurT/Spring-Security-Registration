package com.jaka.springsecurityregistration.Exception.Exceptions;

public class UserNotVerifiedException extends RuntimeException{
  public UserNotVerifiedException(String message) {
    super(message);
  }
}
