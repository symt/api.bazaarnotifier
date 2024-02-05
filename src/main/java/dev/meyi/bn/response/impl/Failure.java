package dev.meyi.bn.response.impl;

import dev.meyi.bn.response.Response;

public class Failure extends Response {
  public Failure(String response) {
    this.success = false;
    this.response = response;
  }

  public boolean getSuccess() {
    return this.success;
  }

  public String getResponse() {
    return this.response;
  }
}
