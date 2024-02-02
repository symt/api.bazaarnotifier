package dev.meyi.bn.controller;

import dev.meyi.bn.entity.Player;
import dev.meyi.bn.repository.PlayerRepository;
import dev.meyi.bn.response.Response;
import dev.meyi.bn.response.impl.Failure;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollectionController {
  private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");

  private final PlayerRepository repository;

  public CollectionController(PlayerRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/collections/{uuid}")
  public Response getCollections(@PathVariable String uuid) {
    if (UUID_PATTERN.matcher(uuid).matches()) {
      Player p = repository.findByUuid(uuid);
      if (p == null) repository.save((p = new Player(uuid)));
      p.increment();
    }

    return new Failure();
  }
}
