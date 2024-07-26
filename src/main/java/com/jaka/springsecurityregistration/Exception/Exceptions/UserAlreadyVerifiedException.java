package com.jaka.springsecurityregistration.Exception.Exceptions;

public class UserAlreadyVerifiedException extends RuntimeException{
  public UserAlreadyVerifiedException(String message) {
    super(message);
  }
}
