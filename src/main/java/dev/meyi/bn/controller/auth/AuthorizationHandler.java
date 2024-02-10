package dev.meyi.bn.controller.auth;

import dev.meyi.bn.config.SecretsManager;
import dev.meyi.bn.constants.AuthorizationCode;
import dev.meyi.bn.entity.AuthToken;
import dev.meyi.bn.entity.Player;
import dev.meyi.bn.repository.AuthRepository;
import dev.meyi.bn.repository.PlayerRepository;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * Handles the authorization logic for all controllers.
 * TODO: Replace this with some sort of group permission system for more access to admin panel
 */
public class AuthorizationHandler {

  /**
   * Basic authorization checker for admin panel
   *
   * @param authHeaderString the authorization header provided in the request
   * @param username desired individual being authenticated
   * @param password desired key for the individual
   * @return whether the person is authorized
   */
  public static boolean isAuthorized(String authHeaderString, String username, String password) {
    if (authHeaderString != null && authHeaderString.startsWith("Basic ")) {
      String token = new String(Base64.getDecoder().decode(authHeaderString.substring(6)),
          StandardCharsets.UTF_8);
      String[] parts = token.split(":");

      return (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password));
    }

    return false;
  }

  /**
   * This is awful code, don't do it this way... Checks if player provided a valid matching api key
   * with minecraft uuid and provides the authorization code and the player if it was found
   *
   * @param authHeaderString basic auth header
   * @return Entry with Authorization code enum and player (or the authorization code id)
   */
  public static Entry<AuthorizationCode, ?> getAuthorizedUser(String authHeaderString,
      AuthRepository ar, PlayerRepository pr) {
    if (authHeaderString != null && authHeaderString.startsWith("Basic ")) {
      String token = new String(Base64.getDecoder().decode(authHeaderString.substring(6)),
          StandardCharsets.UTF_8);
      String[] parts = token.split(":");

      AuthToken at;
      if (parts.length == 2 &&
          parts[0].length() == 32 &&
          parts[1].length() == 32 &&
          (at = ar.findByToken(parts[1])) != null
      ) {

        Optional<Player> p = pr.findById(at.getPlayerId());

        if (p.isPresent()) {
          if (!SecretsManager.getInstance().getActiveProfile().equalsIgnoreCase("TST") && p.get()
              .isRateLimited()) {
            return AuthorizationCode.RATE_LIMITED.entry();
          }

          p.get().request();
          pr.save(p.get());

          return Map.entry(AuthorizationCode.VALID, p.get());
        }

        return AuthorizationCode.MISSING_ACCOUNT.entry();
      }
    }

    return AuthorizationCode.BAD_TOKEN.entry();
  }
}
