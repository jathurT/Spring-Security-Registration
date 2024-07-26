package com.jaka.springsecurityregistration.Repository;


import com.jaka.springsecurityregistration.Entity.PasswordResetToken;
import com.jaka.springsecurityregistration.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
  PasswordResetToken findByUser(User user);
  PasswordResetToken findByToken(String token);
}
