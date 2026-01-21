package com.viridial.organization.forms;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Form DTO for TeamEntity.
 */
public class TeamForm {
    private Long id;
    private String internalCode;
    private String externalCode;
    private String name;
    private String description;
    private String email;
    private String website;
    private String taxId;
    private String vatNumber;
    private String logoUrl;
    private LocalDate foundedDate;
    private Integer employeeCount;
    private String notes;
    private boolean active;
    private Long parentId;
    private Long teamTypeId;
    private Long industryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getInternalCode() { return internalCode; }
    public void setInternalCode(String internalCode) { this.internalCode = internalCode; }
    public String getExternalCode() { return externalCode; }
    public void setExternalCode(String externalCode) { this.externalCode = externalCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }
    public String getVatNumber() { return vatNumber; }
    public void setVatNumber(String vatNumber) { this.vatNumber = vatNumber; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public LocalDate getFoundedDate() { return foundedDate; }
    public void setFoundedDate(LocalDate foundedDate) { this.foundedDate = foundedDate; }
    public Integer getEmployeeCount() { return employeeCount; }
    public void setEmployeeCount(Integer employeeCount) { this.employeeCount = employeeCount; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Long getTeamTypeId() { return teamTypeId; }
    public void setTeamTypeId(Long teamTypeId) { this.teamTypeId = teamTypeId; }
    public Long getIndustryId() { return industryId; }
    public void setIndustryId(Long industryId) { this.industryId = industryId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

