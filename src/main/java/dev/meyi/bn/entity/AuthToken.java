package dev.meyi.bn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

/**
 * Database Table for the AuthToken
 */
@Entity
@Table(name = "authTokens")
public class AuthToken {

  private @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

  private @JoinColumn(name = "players.id")
  @Column(unique = true) Long playerId; // one api key per playerId

  @Column(unique = true)
  private String token; // ensures no collisions of API keys (however unlikely)

  public AuthToken(Long playerId, String token) {
    this.playerId = playerId;
    this.token = token;
  }

  public AuthToken() {

  }

  public Long getPlayerId() {
    return id;
  }

  public String getToken() {
    return this.token;
  }
}
