package dev.villanidev.sportstracker.operations.repository;

import dev.villanidev.sportstracker.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
}
