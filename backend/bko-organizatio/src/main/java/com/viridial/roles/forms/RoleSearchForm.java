package com.viridial.roles.forms;

import com.viridial.common.form.SearchForm;

/**
 * Search form for RoleEntity.
 */
public class RoleSearchForm extends SearchForm {
    private String code;
    private String label;
    private Boolean active;
    private Boolean admin;
    private Long parentId;

    // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public Boolean getAdmin() { return admin; }
    public void setAdmin(Boolean admin) { this.admin = admin; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
}

