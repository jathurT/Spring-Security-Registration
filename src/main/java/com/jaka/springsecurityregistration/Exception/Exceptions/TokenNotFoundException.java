package com.jaka.SpringRegistration.Exception.Exceptions;

public class TokenNotFoundException extends RuntimeException{
  public TokenNotFoundException(String message) {
    super(message);
  }
}
