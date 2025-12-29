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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "match_events")
public class MatchEvent {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", length = 30)
    private MatchEventType eventType;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "minute")
    private Integer minute;

    // Store JSON in PostgreSQL jsonb column, using Hibernate's JSON support.
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "details", columnDefinition = "jsonb")
    private String details;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "processed_for_streaming")
    private Boolean processedForStreaming = Boolean.FALSE;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public MatchEventType getEventType() {
        return eventType;
    }

    public void setEventType(MatchEventType eventType) {
        this.eventType = eventType;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getProcessedForStreaming() {
        return processedForStreaming;
    }

    public void setProcessedForStreaming(Boolean processedForStreaming) {
        this.processedForStreaming = processedForStreaming;
    }
}
