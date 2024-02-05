package dev.meyi.bn.repository;

import dev.meyi.bn.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<AuthToken, Long> {
  AuthToken findByToken(String token);
}
