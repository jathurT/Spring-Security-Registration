package com.jaka.SpringRegistration.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class PasswordResetToken {
  private static final int EXPIRATION_TIME = 10;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String token;

  private Date expirationTime;

  @JoinColumn(
    name = "user_id",
    nullable = false,
    foreignKey = @ForeignKey(name = "FK_USER_PASSWORD_RESET_TOKEN")
  )
  @OneToOne(fetch = FetchType.EAGER)
  private User user;

  public PasswordResetToken(User user, String token) {
    super();
    this.user = user;
    this.token = token;
    this.expirationTime = calculateExpirationDate();
  }

  private Date calculateExpirationDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(new Date().getTime());
    calendar.add(Calendar.MINUTE, PasswordResetToken.EXPIRATION_TIME);
    return new Date((calendar.getTime().getTime()));
  }
}
