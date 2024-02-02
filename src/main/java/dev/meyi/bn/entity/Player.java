package dev.meyi.bn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Player {

  private @Id @GeneratedValue Long id;
  private String uuid;
  private long requestCount;

  public Player(String uuid) {
    this.uuid = uuid;
  }

  public Player() {
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Id
  public Long getId() {
    return id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public void increment() {
    requestCount++;
  }
}
