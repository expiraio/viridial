package com.viridial.countries.forms;

import com.viridial.common.form.SearchForm;

/**
 * Search form for TimezoneEntity.
 */
public class TimezoneSearchForm extends SearchForm {
    private String code;
    private String name;
    private Boolean active;
    private Boolean usesDst;

    // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public Boolean getUsesDst() { return usesDst; }
    public void setUsesDst(Boolean usesDst) { this.usesDst = usesDst; }
}

