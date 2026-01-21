package com.viridial.organization.forms;

import com.viridial.common.form.SearchForm;

/**
 * Search form for TeamEntity.
 */
public class TeamSearchForm extends SearchForm {
    private String internalCode;
    private String name;
    private String email;
    private Boolean active;
    private Long teamTypeId;
    private Long industryId;
    private Long parentId;

    // Getters and Setters
    public String getInternalCode() { return internalCode; }
    public void setInternalCode(String internalCode) { this.internalCode = internalCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public Long getTeamTypeId() { return teamTypeId; }
    public void setTeamTypeId(Long teamTypeId) { this.teamTypeId = teamTypeId; }
    public Long getIndustryId() { return industryId; }
    public void setIndustryId(Long industryId) { this.industryId = industryId; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
}

