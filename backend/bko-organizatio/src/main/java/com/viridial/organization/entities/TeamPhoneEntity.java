package com.viridial.organization.entities;

import com.viridial.common.entities.BaseEntity;
import com.viridial.referentiel.entities.ReferentialEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "org_team_phone",
    indexes = {
        @Index(name = "idx_team_phone_team_id", columnList = "team_id"),
        @Index(name = "idx_team_phone_type_id", columnList = "type_id"),
        @Index(name = "idx_team_phone_active", columnList = "team_id, active"),
        @Index(name = "idx_team_phone_primary", columnList = "team_id, primary_phone"),
        @Index(name = "idx_team_phone_verified", columnList = "verified")
    },
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_team_phone_unique",
            columnNames = {"country_code", "number", "team_id"}
        )
    }
)
public class TeamPhoneEntity extends BaseEntity {

    @Column(name = "country_code", nullable = false, length = 5)
    @NotBlank
    @Size(max = 5)
    private String countryCode;

    @Column(name = "number", nullable = false, length = 20)
    @NotBlank
    @Size(max = 20)
    private String number;

    @Column(name = "extension", length = 10)
    @Size(max = 10)
    private String extension;

    @Column(name = "formatted_number", length = 50)
    @Size(max = 50)
    private String formattedNumber;

    @Column(name = "type_id", nullable = false)
    @NotNull
    private Long typeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "type_id", nullable = false, insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_team_phone_type"))
    private ReferentialEntity type;

    @Column(name = "primary_phone", nullable = false)
    private boolean primaryPhone = false;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "verified", nullable = false)
    private boolean verified = false;

    @Column(name = "last_verified_at")
    @PastOrPresent
    private LocalDateTime lastVerifiedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "team_id", nullable = false)
    @NotNull
    private Long teamId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false, insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_team_phone_team"))
    private TeamEntity team;

    // Getters and Setters
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFormattedNumber() {
        return formattedNumber;
    }

    public void setFormattedNumber(String formattedNumber) {
        this.formattedNumber = formattedNumber;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public ReferentialEntity getType() {
        return type;
    }

    public void setType(ReferentialEntity type) {
        this.type = type;
        this.typeId = type != null ? type.getId() : null;
    }

    public boolean isPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(boolean primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public LocalDateTime getLastVerifiedAt() {
        return lastVerifiedAt;
    }

    public void setLastVerifiedAt(LocalDateTime lastVerifiedAt) {
        this.lastVerifiedAt = lastVerifiedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public TeamEntity getTeam() {
        return team;
    }

    public void setTeam(TeamEntity team) {
        this.team = team;
        this.teamId = team != null ? team.getId() : null;
    }
}

