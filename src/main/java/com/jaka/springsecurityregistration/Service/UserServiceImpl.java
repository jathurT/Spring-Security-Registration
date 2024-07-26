package com.jaka.springsecurityregistration.Service;


import com.jaka.springsecurityregistration.Entity.PasswordResetToken;
import com.jaka.springsecurityregistration.Entity.User;
import com.jaka.springsecurityregistration.Entity.VerificationToken;
import com.jaka.springsecurityregistration.Exception.Exceptions.*;
import com.jaka.springsecurityregistration.Model.UserModel;
import com.jaka.springsecurityregistration.Repository.PasswordResetTokenRepository;
import com.jaka.springsecurityregistration.Repository.UserRepository;
import com.jaka.springsecurityregistration.Repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordResetTokenRepository passwordResetTokenRepository;

  @Autowired
  private VerificationTokenRepository verificationTokenRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public void saveVerificationTokenForUser(String token, User user) {
    try {
      VerificationToken verificationToken = new VerificationToken(user, token);
      verificationTokenRepository.save(verificationToken);
    } catch (DataAccessException e) {
      throw new DatabaseException("Verification token not found:" + e);
    } catch (Exception e) {
      log.info("Internal Error" + e);
    }
  }

  @Override
  public User registerUser(UserModel userModel) {
    if (userModel == null) {
      throw new IllegalArgumentException("User model cannot be null.");
    }

    if (findUserByEmail(userModel.getEmail()) != null) {
      throw new UserAlreadyExistException("There is an account with that email address: " + userModel.getEmail());
    }

    User user = User.builder()
      .email(userModel.getEmail())
      .password(passwordEncoder.encode(userModel.getPassword()))
      .firstName(userModel.getFirstName())
      .lastName(userModel.getLastName())
      .role("User")
      .build();

    try {
      userRepository.save(user);
    } catch (DataAccessException e) {
      throw new DatabaseException("Error saving user to the database");
    } catch (ConstraintViolationException e) {
      throw new UserAlreadyExistException("There is an account with that email address: " + userModel.getEmail());
    }
    return user;
  }

  @Override
  public String validateVerificationToken(String token) {
    try {
      VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
      if (verificationToken == null)
        throw new TokenNotFoundException("Token not found");
      User user = verificationToken.getUser();
      if (user == null) {
        throw new UserNotFoundException("User not found for the token");
      }
      Calendar calendar = Calendar.getInstance();
      if (verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
        verificationTokenRepository.delete(verificationToken);
        throw new TokenExpiredException("Token expired");
      }
      user.setEnabled(true);
      userRepository.save(user);
      return "Valid";
    } catch (TokenNotFoundException | TokenExpiredException e) {
      return e.getMessage();
    } catch (DataAccessException e) {
      log.info("Database Error" + e);
      return "Database Error";
    } catch (Exception e) {
      log.info("Error" + e);
      return "Error";
    }
  }

  @Override
  public VerificationToken generateNewVerificationToken(String oldToken) {
    try {
      VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
      if (verificationToken == null) {
        throw new TokenNotFoundException("Old token not found: " + oldToken);
      }
      User user = verificationToken.getUser();
      if (user.getEnabled()) {
        throw new UserAlreadyVerifiedException("User has been already verified");
      }
      verificationToken.setToken(UUID.randomUUID().toString());
      verificationTokenRepository.save(verificationToken);
      return verificationToken;
    } catch (TokenNotFoundException | UserAlreadyVerifiedException e) {
      throw e;
    } catch (DataAccessException e) {
      throw new DatabaseException("Database access error occurred while generating a new verification token" + e.getMessage());
    } catch (Exception e) {
      throw new RuntimeException("An unexpected error occurred while generating a new verification token" + e.getMessage());
    }
  }

  @Override
  public User findUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public void createPasswordResetTokenForUser(User user, String token) {
    try {
      if (!user.getEnabled())
        throw new UserNotVerifiedException("This user has been not verified");
      PasswordResetToken existingToken = passwordResetTokenRepository.findByUser(user);
      if (existingToken != null) {
        passwordResetTokenRepository.delete(existingToken);
      }
      PasswordResetToken newPasswordResetToken = new PasswordResetToken(user,token);
      passwordResetTokenRepository.save(newPasswordResetToken);
    } catch (UserNotVerifiedException e) {
      throw e;
    } catch (DataAccessException e) {
      throw new DatabaseException("Database access error: " + e.getMessage());
    } catch (Exception e) {
      throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
    }
  }

  @Override
  public String validatePasswordResetToken(String token) {
    try {
      PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
      if (passwordResetToken == null)
        throw new TokenNotFoundException("Token not found");
      User user = passwordResetToken.getUser();
      if (user == null) {
        throw new UserNotFoundException("User not found for the token");
      }
      Calendar calendar = Calendar.getInstance();
      if (passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
        passwordResetTokenRepository.delete(passwordResetToken);
        throw new TokenExpiredException("Token expired");
      }
      return "Valid";
    } catch (TokenNotFoundException | TokenExpiredException | UserNotFoundException e) {
      return e.getMessage();
    } catch (DataAccessException e) {
      return "Database Error" + e.getMessage();
    } catch (Exception e) {
      return "Error" + e.getMessage();
    }
  }

  @Override
  public User getUserByPasswordResetToken(String token) {
    try {
      Optional<User> userOptional = Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
      if (userOptional.isEmpty()) {
        throw new UserNotFoundException("No user found for the given token");
      }
      return userOptional.get();
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while retrieving the user by password reset token" + e.getMessage());
    }
  }

  @Override
  public void changePassword(User user, String newPassword) {
    if (user == null || newPassword == null)
      throw new ArgumentNotFoundException("User object or new password is null or empty");
    try {
      user.setPassword(passwordEncoder.encode(newPassword));
      userRepository.save(user);
    } catch (DataAccessException e) {
      throw new DatabaseException("Database access error occurred while updating password" + e.getMessage());
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while updating password" + e);
    }
  }

  @Override
  public boolean checkIfValidOldPassword(User user, String oldPassword) {
    try {
      if (user == null || oldPassword == null)
        throw new ArgumentNotFoundException("User object or old password is null or empty");
      if (!user.getEnabled())
        throw new UserNotVerifiedException("This user has been not verified");
      return passwordEncoder.matches(oldPassword, user.getPassword());
    } catch (IllegalArgumentException | UserNotVerifiedException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException("Failed to validate password due to an unexpected error" + e);
    }
  }
}
