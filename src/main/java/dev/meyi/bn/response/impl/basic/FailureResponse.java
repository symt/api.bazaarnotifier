package dev.meyi.bn.response.impl.basic;

import dev.meyi.bn.response.SimpleResponse;

/**
 * Response for when the API has any sort of issue and "success": false is required
 */
public class FailureResponse extends SimpleResponse {

  public FailureResponse(String response) {
    this.success = false;
    this.response = response;
  }
}
