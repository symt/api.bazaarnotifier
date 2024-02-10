package dev.meyi.bn.response;

/**
 * A simpler response object for cases where only a String is required for the response
 */
public abstract class SimpleResponse extends Response<String> {

  protected boolean success;
  protected String response;

  public String getResponse() {
    return this.response;
  }

  public boolean getSuccess() {
    return this.success;
  }
}
