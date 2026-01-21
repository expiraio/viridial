package com.viridial.countries.forms;

import java.time.LocalDateTime;

/**
 * Form DTO for TimezoneEntity.
 */
public class TimezoneForm {
    private Long id;
    private String code;
    private String name;
    private String abbreviation;
    private String utcOffset;
    private String dstOffset;
    private boolean usesDst;
    private String description;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAbbreviation() { return abbreviation; }
    public void setAbbreviation(String abbreviation) { this.abbreviation = abbreviation; }
    public String getUtcOffset() { return utcOffset; }
    public void setUtcOffset(String utcOffset) { this.utcOffset = utcOffset; }
    public String getDstOffset() { return dstOffset; }
    public void setDstOffset(String dstOffset) { this.dstOffset = dstOffset; }
    public boolean isUsesDst() { return usesDst; }
    public void setUsesDst(boolean usesDst) { this.usesDst = usesDst; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

