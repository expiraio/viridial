package com.viridial.countries.entities;

import com.viridial.common.entities.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(
    name = "com_timezone",
    indexes = {
        @Index(name = "idx_timezone_code", columnList = "code"),
        @Index(name = "idx_timezone_name", columnList = "name"),
        @Index(name = "idx_timezone_active", columnList = "active"),
        @Index(name = "idx_timezone_utc_offset", columnList = "utc_offset")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_timezone_code", columnNames = "code"),
        @UniqueConstraint(name = "uk_timezone_name", columnNames = "name")
    }
)
public class TimezoneEntity extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true, length = 50)
    @NotBlank
    @Size(max = 50)
    private String code; // e.g., "Europe/Paris", "America/New_York"

    @Column(name = "name", nullable = false, unique = true, length = 100)
    @NotBlank
    @Size(max = 100)
    private String name; // Display name, e.g., "Central European Time"

    @Column(name = "abbreviation", length = 10)
    @Size(max = 10)
    private String abbreviation; // e.g., "CET", "EST", "PST"

    @Column(name = "utc_offset", length = 10)
    @Size(max = 10)
    private String utcOffset; // e.g., "+01:00", "-05:00", "+00:00"

    @Column(name = "dst_offset", length = 10)
    @Size(max = 10)
    private String dstOffset; // Daylight Saving Time offset

    @Column(name = "uses_dst", nullable = false)
    private boolean usesDst = false;

    @Column(name = "description", length = 500)
    @Size(max = 500)
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(String utcOffset) {
        this.utcOffset = utcOffset;
    }

    public String getDstOffset() {
        return dstOffset;
    }

    public void setDstOffset(String dstOffset) {
        this.dstOffset = dstOffset;
    }

    public boolean isUsesDst() {
        return usesDst;
    }

    public void setUsesDst(boolean usesDst) {
        this.usesDst = usesDst;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

