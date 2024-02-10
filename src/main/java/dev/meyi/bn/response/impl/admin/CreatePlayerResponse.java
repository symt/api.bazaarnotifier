package dev.meyi.bn.response.impl.admin;

import dev.meyi.bn.response.SimpleResponse;

/**
 * Response object for /admin/create-player
 */
public class CreatePlayerResponse extends SimpleResponse {

  public CreatePlayerResponse(String api) {
    this.success = true;
    this.response = api;
  }
}
