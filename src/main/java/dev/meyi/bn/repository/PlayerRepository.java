package dev.meyi.bn.repository;

import dev.meyi.bn.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
  Player findByUuid(String uuid);
}