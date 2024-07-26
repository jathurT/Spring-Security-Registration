package com.jaka.SpringRegistration.Service;

import com.jaka.SpringRegistration.Entity.User;
import com.jaka.SpringRegistration.Entity.VerificationToken;
import com.jaka.SpringRegistration.Model.UserModel;

public interface UserService {
  void saveVerificationTokenForUser(String token, User user);
  User registerUser(UserModel userModel);
  String validateVerificationToken(String token);
  VerificationToken generateNewVerificationToken(String oldToken);
  User findUserByEmail(String email);
  void createPasswordResetTokenForUser(User user, String token);
  String validatePasswordResetToken(String token);
  User getUserByPasswordResetToken(String token);
  void changePassword(User user, String newPassword);
  boolean checkIfValidOldPassword(User user, String oldPassword);
}
