package dev.villanidev.sportstracker.operations.repository;

import dev.villanidev.sportstracker.model.Player;
import dev.villanidev.sportstracker.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {

    List<Player> findByTeam(Team team);

    long countByTeam(Team team);
}
