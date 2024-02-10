package dev.meyi.bn.constants;

/**
 * Constant strings aggregated for ease of use in the application
 */
public class ResponseText {

  public static final String BAD_TOKEN = "Invalid authentication token provided";
  public static final String BAD_UUID = "Invalid UUID provided. Either the format was wrong or the UUID isn't tied to the correct account";
  public static final String NO_ACTIVE_ACCOUNT = "No account has been tied to this authentication token";
  public static final String RATE_LIMITED = "This player is currently rate-limited. Requests are currently 1/hour";
}
