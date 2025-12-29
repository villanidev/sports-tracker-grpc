package dev.villanidev.sportstracker.operations.repository;

import dev.villanidev.sportstracker.model.Match;
import dev.villanidev.sportstracker.model.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {

    List<Match> findByStatus(MatchStatus status);
}
