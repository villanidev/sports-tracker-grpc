package dev.villanidev.sportstracker.streaming.repository;

import dev.villanidev.sportstracker.model.Match;
import dev.villanidev.sportstracker.model.MatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface MatchEventStreamingRepository extends JpaRepository<MatchEvent, UUID> {

    List<MatchEvent> findByMatchOrderByCreatedAtAsc(Match match);

    List<MatchEvent> findByProcessedForStreamingFalseAndCreatedAtAfterOrderByCreatedAtAsc(Instant createdAfter);
}
