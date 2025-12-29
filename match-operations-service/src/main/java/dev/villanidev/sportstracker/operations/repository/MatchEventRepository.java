package dev.villanidev.sportstracker.operations.repository;

import dev.villanidev.sportstracker.model.Match;
import dev.villanidev.sportstracker.model.MatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchEventRepository extends JpaRepository<MatchEvent, UUID> {

    List<MatchEvent> findTop20ByOrderByCreatedAtDesc();

    List<MatchEvent> findByMatchOrderByCreatedAtAsc(Match match);

    void deleteByMatch(Match match);
}
