package com.jaka.springsecurityregistration.Exception.Exceptions;

public class TokenExpiredException extends RuntimeException{
  public TokenExpiredException(String message) {
    super(message);
  }
}
