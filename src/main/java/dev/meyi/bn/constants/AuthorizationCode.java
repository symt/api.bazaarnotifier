package dev.meyi.bn.constants;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Used by the AuthorizationHandler to provide information on failure reason (or success)
 */
public enum AuthorizationCode {
  VALID(0),
  BAD_TOKEN(-1),
  MISSING_ACCOUNT(-2),
  RATE_LIMITED(-3);

  public final long id;

  AuthorizationCode(int id) {
    this.id = id;
  }

  public Entry<AuthorizationCode, Long> entry() {
    return Map.entry(this, this.id);
  }
}
