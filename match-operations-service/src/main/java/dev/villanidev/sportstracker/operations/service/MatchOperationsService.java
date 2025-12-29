package dev.villanidev.sportstracker.operations.service;

import dev.villanidev.sportstracker.model.*;
import dev.villanidev.sportstracker.operations.repository.MatchEventRepository;
import dev.villanidev.sportstracker.operations.repository.MatchRepository;
import dev.villanidev.sportstracker.operations.repository.PlayerRepository;
import dev.villanidev.sportstracker.operations.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;


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

        Instant now = Instant.now();

        Match match = new Match();
        match.setHomeTeam(home);
        match.setAwayTeam(away);
        match.setStatus(MatchStatus.LIVE);
        match.setHomeScore(0);
        match.setAwayScore(0);
        match.setStartedAt(now);

        // Initialize clock: match already "in progress" around minute 1
        match.setClockState(MatchClockState.RUNNING);
        match.setClockOffsetSeconds(60); // 1 minute played
        match.setClockLastStartedAt(now);
        match.setCurrentMinute(1);

        // Start in demo mode for the seeded match so the simulator keeps working
        match.setDemoModeEnabled(Boolean.TRUE);

        matchRepository.save(match);
    }

    public List<Match> findLiveMatches() {
        return matchRepository.findByStatus(MatchStatus.LIVE);
    }

    public List<Match> findAllMatches() {
        return matchRepository.findAll();
    }

    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }

    public Team createTeam(String name) {
        Team team = new Team();
        team.setName(name);
        return teamRepository.save(team);
    }

    public void deleteTeam(UUID teamId) {
        teamRepository.findById(teamId).ifPresent(team -> {
            boolean inMatches = matchRepository.existsByHomeTeamOrAwayTeam(team, team);
            long playerCount = playerRepository.countByTeam(team);
            if (!inMatches && playerCount == 0) {
                teamRepository.delete(team);
            }
        });
    }

    public void renameTeam(UUID teamId, String newName) {
        teamRepository.findById(teamId).ifPresent(team -> {
            team.setName(newName);
            teamRepository.save(team);
        });
    }

    public List<MatchEvent> findLatestEvents() {
        return matchEventRepository.findTop20ByOrderByCreatedAtDesc();
    }

    public List<MatchEvent> findEventsForMatch(Match match) {
        return matchEventRepository.findByMatchOrderByCreatedAtAsc(match);
    }

    public void deleteMatch(UUID matchId) {
        matchRepository.findById(matchId).ifPresent(match -> {
            matchEventRepository.deleteByMatch(match);
            matchRepository.delete(match);
        });
    }

    public Optional<Match> findMatchById(UUID id) {
        return matchRepository.findById(id);
    }

    public Match createMatch(UUID homeTeamId, UUID awayTeamId) {
        Team home = teamRepository.findById(homeTeamId)
                .orElseThrow(() -> new IllegalArgumentException("Home team not found"));
        Team away = teamRepository.findById(awayTeamId)
                .orElseThrow(() -> new IllegalArgumentException("Away team not found"));

        Match match = new Match();
        match.setHomeTeam(home);
        match.setAwayTeam(away);
        match.setStatus(MatchStatus.SCHEDULED);
        match.setHomeScore(0);
        match.setAwayScore(0);
        match.setCurrentMinute(0);
        match.setClockState(MatchClockState.NOT_STARTED);
        match.setClockOffsetSeconds(0);
        match.setDemoModeEnabled(Boolean.FALSE);

        return matchRepository.save(match);
    }

    public void toggleDemoMode(UUID matchId, boolean enabled) {
        matchRepository.findById(matchId).ifPresent(match -> {
            match.setDemoModeEnabled(enabled);
            matchRepository.save(match);
        });
    }

    public void startMatch(UUID matchId) {
        matchRepository.findById(matchId).ifPresent(match -> {
            Instant now = Instant.now();
            match.setStatus(MatchStatus.LIVE);
            match.setStartedAt(now);
            match.setClockState(MatchClockState.RUNNING);
            if (match.getClockOffsetSeconds() == null) {
                match.setClockOffsetSeconds(0);
            }
            match.setClockLastStartedAt(now);
            computeAndUpdateCurrentMinute(match, now);
            matchRepository.save(match);
        });
    }

    public void pauseMatch(UUID matchId) {
        matchRepository.findById(matchId).ifPresent(match -> {
            if (match.getClockState() == MatchClockState.RUNNING) {
                Instant now = Instant.now();
                computeAndUpdateCurrentMinute(match, now);
                match.setClockState(MatchClockState.PAUSED);
                matchRepository.save(match);
            }
        });
    }

    public void resumeMatch(UUID matchId) {
        matchRepository.findById(matchId).ifPresent(match -> {
            if (match.getClockState() == MatchClockState.PAUSED || match.getClockState() == MatchClockState.HALF_TIME) {
                Instant now = Instant.now();
                match.setClockState(MatchClockState.RUNNING);
                match.setClockLastStartedAt(now);
                matchRepository.save(match);
            }
        });
    }

    public void halfTime(UUID matchId) {
        matchRepository.findById(matchId).ifPresent(match -> {
            Instant now = Instant.now();
            computeAndUpdateCurrentMinute(match, now);
            match.setStatus(MatchStatus.HALF_TIME);
            match.setClockState(MatchClockState.HALF_TIME);
            matchRepository.save(match);
        });
    }

    public void finishMatch(UUID matchId) {
        matchRepository.findById(matchId).ifPresent(match -> {
            Instant now = Instant.now();
            computeAndUpdateCurrentMinute(match, now);
            match.setStatus(MatchStatus.FINISHED);
            match.setClockState(MatchClockState.FINISHED);
            match.setFinishedAt(now);
            matchRepository.save(match);
        });
    }

    public MatchEvent logRandomEventForRandomLiveMatch() {
        List<Match> liveMatches = findLiveMatches();
        if (liveMatches.isEmpty()) {
            return null;
        }

        Match match = liveMatches.get(random.nextInt(liveMatches.size()));

        MatchEventType type = pickRandomEventType();
        boolean forHomeTeam = random.nextBoolean();

        return createEvent(match, type, forHomeTeam, true);
    }

    public MatchEvent logManualEvent(UUID matchId, String teamSide, MatchEventType type) {
        Optional<Match> optionalMatch = matchRepository.findById(matchId);
        if (optionalMatch.isEmpty()) {
            return null;
        }

        Match match = optionalMatch.get();
        boolean forHomeTeam = "HOME".equalsIgnoreCase(teamSide);
        return createEvent(match, type, forHomeTeam, false);
    }

    private MatchEvent createEvent(Match match, MatchEventType type, boolean forHomeTeam, boolean generated) {

        Team team = forHomeTeam ? match.getHomeTeam() : match.getAwayTeam();
        List<Player> players = playerRepository.findByTeam(team);
        Player player = players.isEmpty() ? null : players.get(random.nextInt(players.size()));

        int minute = computeAndUpdateCurrentMinute(match, Instant.now());

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
            "\"generated\":" + (generated ? "true" : "false") +
            "}";
        event.setDetails(jsonDetails);

        event.setCreatedAt(Instant.now());
        event.setProcessedForStreaming(Boolean.FALSE);

        matchRepository.save(match);
        return matchEventRepository.save(event);
    }

    private int computeAndUpdateCurrentMinute(Match match, Instant now) {
        Integer offsetSeconds = match.getClockOffsetSeconds();
        if (offsetSeconds == null) {
            offsetSeconds = 0;
        }

        if (match.getClockState() == MatchClockState.RUNNING && match.getClockLastStartedAt() != null) {
            long delta = Duration.between(match.getClockLastStartedAt(), now).getSeconds();
            if (delta > 0) {
                offsetSeconds += (int) delta;
                match.setClockOffsetSeconds(offsetSeconds);
                match.setClockLastStartedAt(now);
            }
        }

        int minute = Math.max(0, offsetSeconds / 60);
        match.setCurrentMinute(minute);
        return minute;
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
