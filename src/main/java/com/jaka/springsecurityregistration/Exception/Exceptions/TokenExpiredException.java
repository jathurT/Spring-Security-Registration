package com.jaka.SpringRegistration.Exception.Exceptions;

public class TokenExpiredException extends RuntimeException{
  public TokenExpiredException(String message) {
    super(message);
  }
}
