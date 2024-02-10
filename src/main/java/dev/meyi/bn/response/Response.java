package dev.meyi.bn.response;

/**
 * Generic response object used in the implementation of all other Response objects
 */
public abstract class Response<T> {

  protected boolean success;
  protected T response;

  public T getResponse() {
    return this.response;
  }

  public boolean getSuccess() {
    return this.success;
  }
}
