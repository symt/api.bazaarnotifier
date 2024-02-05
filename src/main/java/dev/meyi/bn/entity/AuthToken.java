package dev.meyi.bn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name="authTokens")
public class AuthToken {

  private @Id @GeneratedValue Long id;

  private @JoinColumn(name="players.id") Long playerId;

  private String token;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
