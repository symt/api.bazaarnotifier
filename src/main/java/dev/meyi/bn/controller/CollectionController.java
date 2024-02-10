package dev.meyi.bn.controller;

import dev.meyi.bn.constants.AuthorizationCode;
import dev.meyi.bn.constants.ResponseText;
import dev.meyi.bn.controller.auth.AuthorizationHandler;
import dev.meyi.bn.entity.Player;
import dev.meyi.bn.hypixel.CollectionCheck;
import dev.meyi.bn.repository.AuthRepository;
import dev.meyi.bn.repository.PlayerRepository;
import dev.meyi.bn.response.Response;
import dev.meyi.bn.response.impl.CollectionResponse;
import dev.meyi.bn.response.impl.basic.FailureResponse;
import java.util.Map.Entry;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling the collections check used by the BazaarNotifier mod
 */
@RestController
public class CollectionController {

  private final PlayerRepository playerRepository;
  private final AuthRepository authRepository;

  public CollectionController(PlayerRepository playerRepository, AuthRepository authRepository) {
    this.playerRepository = playerRepository;
    this.authRepository = authRepository;
  }

  /**
   * Retrieves the Collections of a player from the Hypixel api and provides it in a form usable by
   * the mod
   *
   * @param authorization Basic authorization header in the form playerUUID:apiKey
   * @return success status and list of strings representing the collections available to a player
   */
  @GetMapping(value = "/collections",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Response<?> getCollections(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
    // gross use of wildcard, but hey, it works... don't mess with success
    Entry<AuthorizationCode, ?> code = AuthorizationHandler.getAuthorizedUser(authorization,
        authRepository, playerRepository);
    return switch (code.getKey()) {
      case BAD_TOKEN -> new FailureResponse(ResponseText.BAD_TOKEN);
      case MISSING_ACCOUNT -> new FailureResponse(ResponseText.NO_ACTIVE_ACCOUNT);
      case RATE_LIMITED -> new FailureResponse(ResponseText.RATE_LIMITED);
      case VALID ->
          new CollectionResponse(CollectionCheck.getCollections((Player) code.getValue()));
    };
  }
}