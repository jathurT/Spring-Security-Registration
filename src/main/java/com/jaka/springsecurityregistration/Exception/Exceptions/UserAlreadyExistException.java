package com.jaka.SpringRegistration.Exception.Exceptions;

public class UserAlreadyExistException extends RuntimeException{
  public UserAlreadyExistException(String message) {
    super(message);
  }
}
