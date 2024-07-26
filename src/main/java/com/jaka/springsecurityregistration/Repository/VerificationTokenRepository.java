package com.jaka.springsecurityregistration.Repository;


import com.jaka.springsecurityregistration.Entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {

  VerificationToken findByToken(String token);
}
