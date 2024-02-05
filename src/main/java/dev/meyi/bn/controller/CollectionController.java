package dev.meyi.bn.controller;

import dev.meyi.bn.constants.ResponseText;
import dev.meyi.bn.entity.AuthToken;
import dev.meyi.bn.entity.Player;
import dev.meyi.bn.repository.AuthRepository;
import dev.meyi.bn.repository.PlayerRepository;
import dev.meyi.bn.response.Response;
import dev.meyi.bn.response.impl.Failure;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollectionController {
  private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");

  private final PlayerRepository playerRepository;
  private final AuthRepository authRepository;

  public CollectionController(PlayerRepository playerRepository, AuthRepository authRepository) {
    this.playerRepository = playerRepository;
    this.authRepository = authRepository;
  }

  @GetMapping("/collections")
  public Response getCollections(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
    if (authorization != null && authorization.startsWith("Basic ")) {
      String token = new String(Base64.getDecoder().decode(authorization.substring(6)), StandardCharsets.UTF_8);
      String[] parts = token.split(":");
      if (!UUID_PATTERN.matcher(parts[0]).matches() || !UUID_PATTERN.matcher(parts[1]).matches()) {
        return new Failure(ResponseText.BAD_UUID);
      }

      Player player;
      AuthToken auth = authRepository.findByToken(parts[1]);
      if (auth == null) return new Failure(ResponseText.BAD_TOKEN);

      Optional<Player> p = playerRepository.findById(auth.getId());

      if (p.isPresent()) {
        if (!parts[0].equals((player = p.get()).getUuid())) {
          return new Failure(ResponseText.BAD_UUID);
        }
      } else {
        return new Failure(ResponseText.NO_ACTIVE_ACCOUNT);
      }

      player.increment();
      playerRepository.save(player);

      // TODO: Implement collections call
    }

    return new Failure(ResponseText.BAD_UUID);
  }
}
