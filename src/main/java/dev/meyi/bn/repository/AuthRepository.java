package dev.meyi.bn.repository;

import dev.meyi.bn.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for AuthTokens with methods to search the table using playerId and token
 */
public interface AuthRepository extends JpaRepository<AuthToken, Long> {

  AuthToken findByToken(String token);

  AuthToken findByPlayerId(Long id);
}
