package com.jaka.SpringRegistration.Repository;

import com.jaka.SpringRegistration.Entity.PasswordResetToken;
import com.jaka.SpringRegistration.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
  PasswordResetToken findByUser(User user);
  PasswordResetToken findByToken(String token);
}
