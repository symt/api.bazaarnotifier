package dev.meyi.bn.controller;

import dev.meyi.bn.config.SecretsManager;
import dev.meyi.bn.constants.ResponseText;
import dev.meyi.bn.controller.auth.AuthorizationHandler;
import dev.meyi.bn.entity.AuthToken;
import dev.meyi.bn.entity.Player;
import dev.meyi.bn.repository.AuthRepository;
import dev.meyi.bn.repository.PlayerRepository;
import dev.meyi.bn.response.Response;
import dev.meyi.bn.response.SimpleResponse;
import dev.meyi.bn.response.impl.admin.CreatePlayerResponse;
import dev.meyi.bn.response.impl.admin.DatabaseDumpResponse;
import dev.meyi.bn.response.impl.basic.FailureResponse;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller for the administration and maintenance side of the application. This interfaces
 * with the discord bot (and admins) to provide information such as the stored database tables and
 * allow admins to create new users / assign new api keys. Currently, the panel is limited to BOT
 * access only
 */
@RestController
public class AdminController {

  private final PlayerRepository playerRepository;
  private final AuthRepository authRepository;

  public AdminController(PlayerRepository playerRepository, AuthRepository authRepository) {
    this.playerRepository = playerRepository;
    this.authRepository = authRepository;
  }

  /**
   * Creates a new player in the system (and deletes the associated API key if it already existed)
   *
   * @param authorization standard Basic authorization header with BOT username and secret bot api
   * key
   * @param payload json object with keys player (which is the uuid of the player without dashes)
   * and the discordId of the player requesting to be added
   * @return success status and the new api key for the created player
   */
  @PostMapping(value = "/admin/create-player",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public SimpleResponse createPlayer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
      @RequestBody Map<String, String> payload) {
    if (AuthorizationHandler.isAuthorized(authorization, "BOT", SecretsManager.getInstance()
        .getBotKey())) {
      Player p;
      if (payload.get("player").length() != 32) {
        return new FailureResponse(ResponseText.BAD_UUID);
      }

      if ((p = playerRepository.findByUuid(payload.get("player"))) == null) {
        p = playerRepository.save(new Player(payload.get("player"), payload.get("discordId")));
      } else if (p.isRateLimited()) {
        // we aren't giving a new api key if the user is currently rate limited
        return new FailureResponse(ResponseText.RATE_LIMITED);
      }

      AuthToken at;
      if ((at = authRepository.findByPlayerId(p.getId())) != null) {
        authRepository.delete(at);
      }

      String newApiKey = UUID.randomUUID().toString().replace("-", "");
      authRepository.save(new AuthToken(p.getId(), newApiKey));

      return new CreatePlayerResponse(newApiKey);
    }

    return new FailureResponse(ResponseText.BAD_TOKEN);
  }

  /**
   * Provides list of all API keys and the associated playerIds
   *
   * @param authorization standard Basic authorization header with BOT username and secret bot api
   * key
   * @return List of AuthTokens
   */
  @GetMapping(value = "/admin/db_dump/auth",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Response<?> dumpAuth(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
    if (AuthorizationHandler.isAuthorized(authorization, "BOT", SecretsManager.getInstance()
        .getBotKey())) {
      return new DatabaseDumpResponse<>(authRepository.findAll());
    }
    return new FailureResponse(ResponseText.BAD_TOKEN);
  }

  /**
   * Provides list of all players in the system
   *
   * @param authorization standard Basic authorization header with BOT username and secret bot api
   * key
   * @return List of Players
   */
  @GetMapping(value = "/admin/db_dump/player",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Response<?> dumpPlayer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
    if (AuthorizationHandler.isAuthorized(authorization, "BOT", SecretsManager.getInstance()
        .getBotKey())) {
      return new DatabaseDumpResponse<>(playerRepository.findAll());
    }
    return new FailureResponse(ResponseText.BAD_TOKEN);
  }
}
