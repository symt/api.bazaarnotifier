package dev.meyi.bn.repository;

import dev.meyi.bn.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for Player with a find by method for UUID
 */
public interface PlayerRepository extends JpaRepository<Player, Long> {

  Player findByUuid(String uuid);
}