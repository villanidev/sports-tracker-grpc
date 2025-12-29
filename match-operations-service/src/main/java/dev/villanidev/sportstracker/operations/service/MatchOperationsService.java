package dev.villanidev.sportstracker.operations.service;

import dev.villanidev.sportstracker.model.*;
import dev.villanidev.sportstracker.operations.repository.MatchEventRepository;
import dev.villanidev.sportstracker.operations.repository.MatchRepository;
import dev.villanidev.sportstracker.operations.repository.PlayerRepository;
import dev.villanidev.sportstracker.operations.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class MatchOperationsService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final MatchEventRepository matchEventRepository;
    private final Random random = new Random();

    public MatchOperationsService(TeamRepository teamRepository,
                                  PlayerRepository playerRepository,
                                  MatchRepository matchRepository,
                                  MatchEventRepository matchEventRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.matchEventRepository = matchEventRepository;
    }

    @PostConstruct
    public void seedDemoDataIfEmpty() {
        if (matchRepository.count() > 0) {
            return;
        }

        Team home = new Team();
        home.setName("FC Home");
        home = teamRepository.save(home);

        Team away = new Team();
        away.setName("SC Away");
        away = teamRepository.save(away);

        for (int i = 1; i <= 11; i++) {
            Player pHome = new Player();
            pHome.setTeam(home);
            pHome.setName("Home Player " + i);
            pHome.setPosition("P" + i);
            playerRepository.save(pHome);

            Player pAway = new Player();
            pAway.setTeam(away);
            pAway.setName("Away Player " + i);
            pAway.setPosition("P" + i);
            playerRepository.save(pAway);
        }

        Match match = new Match();
        match.setHomeTeam(home);
        match.setAwayTeam(away);
        match.setStatus(MatchStatus.LIVE);
        match.setHomeScore(0);
        match.setAwayScore(0);
        match.setCurrentMinute(1);
        match.setStartedAt(Instant.now());
        matchRepository.save(match);
    }

    public List<Match> findLiveMatches() {
        return matchRepository.findByStatus(MatchStatus.LIVE);
    }

    public List<MatchEvent> findLatestEvents() {
        return matchEventRepository.findTop20ByOrderByCreatedAtDesc();
    }

    public List<MatchEvent> findEventsForMatch(Match match) {
        return matchEventRepository.findByMatchOrderByCreatedAtAsc(match);
    }

    public Optional<Match> findMatchById(java.util.UUID id) {
        return matchRepository.findById(id);
    }

    public MatchEvent logRandomEventForRandomLiveMatch() {
        List<Match> liveMatches = findLiveMatches();
        if (liveMatches.isEmpty()) {
            return null;
        }

        Match match = liveMatches.get(random.nextInt(liveMatches.size()));

        MatchEventType type = pickRandomEventType();
        boolean forHomeTeam = random.nextBoolean();

        Team team = forHomeTeam ? match.getHomeTeam() : match.getAwayTeam();
        List<Player> players = playerRepository.findByTeam(team);
        Player player = players.isEmpty() ? null : players.get(random.nextInt(players.size()));

        Integer minute = Optional.ofNullable(match.getCurrentMinute()).orElse(1) + random.nextInt(3) + 1;
        match.setCurrentMinute(minute);

        if (type == MatchEventType.GOAL) {
            if (forHomeTeam) {
                match.setHomeScore(Optional.ofNullable(match.getHomeScore()).orElse(0) + 1);
            } else {
                match.setAwayScore(Optional.ofNullable(match.getAwayScore()).orElse(0) + 1);
            }
        }

        MatchEvent event = new MatchEvent();
        event.setMatch(match);
        event.setEventType(type);
        event.setPlayer(player);
        event.setMinute(minute);

        // Minimal JSON payload for the details jsonb column
        String teamSide = forHomeTeam ? "HOME" : "AWAY";
        String playerName = player != null ? player.getName() : null;
        String jsonDetails = "{" +
            "\"teamSide\":\"" + teamSide + "\"," +
            (playerName != null ? "\"playerName\":\"" + playerName + "\"," : "") +
            "\"generated\":true" +
            "}";
        event.setDetails(jsonDetails);

        event.setCreatedAt(Instant.now());
        event.setProcessedForStreaming(Boolean.FALSE);

        matchRepository.save(match);
        return matchEventRepository.save(event);
    }

    private MatchEventType pickRandomEventType() {
        int roll = random.nextInt(100);
        if (roll < 60) {
            return MatchEventType.GOAL;
        } else if (roll < 85) {
            return MatchEventType.YELLOW_CARD;
        } else if (roll < 95) {
            return MatchEventType.RED_CARD;
        } else {
            return MatchEventType.SUBSTITUTION;
        }
    }
}
