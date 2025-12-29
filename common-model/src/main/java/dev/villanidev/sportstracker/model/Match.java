package dev.villanidev.sportstracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(optional = false)
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MatchStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "clock_state", length = 20)
    private MatchClockState clockState;

    @Column(name = "clock_offset_seconds")
    private Integer clockOffsetSeconds;

    @Column(name = "clock_last_started_at")
    private Instant clockLastStartedAt;

    @Column(name = "home_score")
    private Integer homeScore = 0;

    @Column(name = "away_score")
    private Integer awayScore = 0;

    @Column(name = "current_minute")
    private Integer currentMinute;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @Column(name = "demo_mode_enabled")
    private Boolean demoModeEnabled;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public MatchClockState getClockState() {
        return clockState;
    }

    public void setClockState(MatchClockState clockState) {
        this.clockState = clockState;
    }

    public Integer getClockOffsetSeconds() {
        return clockOffsetSeconds;
    }

    public void setClockOffsetSeconds(Integer clockOffsetSeconds) {
        this.clockOffsetSeconds = clockOffsetSeconds;
    }

    public Instant getClockLastStartedAt() {
        return clockLastStartedAt;
    }

    public void setClockLastStartedAt(Instant clockLastStartedAt) {
        this.clockLastStartedAt = clockLastStartedAt;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

    public Integer getCurrentMinute() {
        return currentMinute;
    }

    public void setCurrentMinute(Integer currentMinute) {
        this.currentMinute = currentMinute;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Boolean getDemoModeEnabled() {
        return demoModeEnabled;
    }

    public void setDemoModeEnabled(Boolean demoModeEnabled) {
        this.demoModeEnabled = demoModeEnabled;
    }
}
