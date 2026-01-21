package com.viridial.countries.forms;

import com.viridial.common.form.SearchForm;

/**
 * Search form for CountryEntity.
 */
public class CountrySearchForm extends SearchForm {
    private String name;
    private String iso2;
    private String iso3;
    private Boolean active;
    private Boolean enabled;
    private Long regionId;
    private Long subRegionId;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIso2() { return iso2; }
    public void setIso2(String iso2) { this.iso2 = iso2; }
    public String getIso3() { return iso3; }
    public void setIso3(String iso3) { this.iso3 = iso3; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public Long getRegionId() { return regionId; }
    public void setRegionId(Long regionId) { this.regionId = regionId; }
    public Long getSubRegionId() { return subRegionId; }
    public void setSubRegionId(Long subRegionId) { this.subRegionId = subRegionId; }
}

