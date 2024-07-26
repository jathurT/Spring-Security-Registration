package com.jaka.SpringRegistration.Repository;

import com.jaka.SpringRegistration.Entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {

  VerificationToken findByToken(String token);
}
