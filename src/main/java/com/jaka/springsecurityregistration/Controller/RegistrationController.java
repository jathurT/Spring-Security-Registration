package com.jaka.springsecurityregistration.Controller;


import com.jaka.springsecurityregistration.Entity.User;
import com.jaka.springsecurityregistration.Entity.VerificationToken;
import com.jaka.springsecurityregistration.Event.RegistrationCompleteEvent;
import com.jaka.springsecurityregistration.Event.ResendVerificationTokenCompleteEvent;
import com.jaka.springsecurityregistration.Event.ResetPasswordCompleteEvent;
import com.jaka.springsecurityregistration.Model.PasswordModel;
import com.jaka.springsecurityregistration.Model.UserModel;
import com.jaka.springsecurityregistration.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
public class RegistrationController {

  private final UserService userService;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  public RegistrationController(UserService userService, ApplicationEventPublisher applicationEventPublisher) {
    this.userService = userService;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @PostMapping("/register")
  public ResponseEntity<String> registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
    log.info("Received registration request for user: {}", userModel.getEmail());
    User user = userService.registerUser(userModel);
    applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationURL(request), request.getLocale()));
    return ResponseEntity.status(HttpStatus.CREATED).body("User has been registered successfully");
  }

  @GetMapping("/verifyRegistration")
  public ResponseEntity<String> verifyRegistration(@RequestParam("token") String token) {
    String result = userService.validateVerificationToken(token);
    if (result.equalsIgnoreCase("valid")) {
      return ResponseEntity.ok("User verified successfully");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
    }
  }

  @GetMapping("/resendVerifyToken")
  public ResponseEntity<String> resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
    VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
    User user = verificationToken.getUser();
    applicationEventPublisher.publishEvent(new ResendVerificationTokenCompleteEvent(user, applicationURL(request), verificationToken, request.getLocale()));
    return ResponseEntity.status(HttpStatus.OK).body("Verification link sent");
  }

  @PostMapping("/resetPassword")
  public ResponseEntity<String> resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {
    User user = userService.findUserByEmail(passwordModel.getEmail());
    String token = UUID.randomUUID().toString();
    userService.createPasswordResetTokenForUser(user, token);
    applicationEventPublisher.publishEvent(new ResetPasswordCompleteEvent(user, applicationURL(request), token, request.getLocale()));
    return ResponseEntity.status(HttpStatus.OK).body("Password reset verification link has been sent successfully");
  }

  @PostMapping("/savePassword")
  public ResponseEntity<String> savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel) {
    String result = userService.validatePasswordResetToken(token);
    if (result.equalsIgnoreCase("valid")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Token");
    }
    User user = userService.getUserByPasswordResetToken(token);
    userService.changePassword(user, passwordModel.getNewPassword());
    return ResponseEntity.status(HttpStatus.OK).body("Updated Password has been saved successfully");
  }

  @PostMapping("/changePassword")
  public ResponseEntity<String> changePassword(@RequestBody PasswordModel passwordModel) {
    User user = userService.findUserByEmail(passwordModel.getEmail());
    if (!userService.checkIfValidOldPassword(user, passwordModel.getOldPassword())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid old password");
    }
    userService.changePassword(user, passwordModel.getNewPassword());
    return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully");
  }

  private String applicationURL(HttpServletRequest request) {
    return "http://" +
            request.getServerName() +
            ":" +
            request.getServerPort() +
            request.getContextPath();
  }
}
