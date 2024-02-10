package dev.meyi.bn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Database Table for Player
 */
@Entity
@Table(name = "players")
public class Player {

  private @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

  @Column(unique = true)
  private String uuid; // dash-less uuid of the player

  @Column(unique = true)
  private String discordId; // discord account tied to the player

  private long requestCount; // number of requests made to the API

  private long lastRequest;

  public Player(String uuid, String discordId) {
    this.uuid = uuid;
    this.discordId = discordId;
  }

  public Player() {
  }

  public Long getId() {
    return this.id;
  }

  public String getUuid() {
    return this.uuid;
  }

  public String getDiscordId() {
    return this.discordId;
  }

  public long getRequestCount() {
    return this.requestCount;
  }

  public long getLastRequest() {
    return this.lastRequest;
  }

  /**
   * Update the last request time for rate limiting purposes and keep track of the number of
   * requests done by a user
   */
  public void request() {
    lastRequest = System.currentTimeMillis();
    requestCount++;
  }

  /**
   * Rate limit a user to one request per hour
   *
   * @return whether the Player is rate limited
   */
  public boolean isRateLimited() {
    return (System.currentTimeMillis() - lastRequest < 3600000);
  }
}
