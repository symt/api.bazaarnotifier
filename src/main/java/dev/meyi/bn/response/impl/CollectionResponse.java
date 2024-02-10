package dev.meyi.bn.response.impl;

import dev.meyi.bn.response.Response;
import java.util.List;


/**
 * Response for collection check providing list of unlocked recipes
 */
public class CollectionResponse extends Response<List<String>> {

  public CollectionResponse(List<String> collections) {
    this.response = collections;
    this.success = this.response != null;
  }
}
