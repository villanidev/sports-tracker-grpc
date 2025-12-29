package dev.villanidev.sportstracker.streaming.repository;

import dev.villanidev.sportstracker.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MatchStreamingRepository extends JpaRepository<Match, UUID> {
}
