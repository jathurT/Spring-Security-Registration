package com.jaka.springsecurityregistration.Exception.Exceptions;

public class TokenNotFoundException extends RuntimeException{
  public TokenNotFoundException(String message) {
    super(message);
  }
}
