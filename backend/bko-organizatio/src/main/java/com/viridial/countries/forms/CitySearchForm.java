package com.viridial.countries.forms;

import com.viridial.common.form.SearchForm;

/**
 * Search form for CityEntity.
 */
public class CitySearchForm extends SearchForm {
    private String name;
    private Boolean active;
    private Boolean capital;
    private Long countryId;
    private Long timezoneId;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public Boolean getCapital() { return capital; }
    public void setCapital(Boolean capital) { this.capital = capital; }
    public Long getCountryId() { return countryId; }
    public void setCountryId(Long countryId) { this.countryId = countryId; }
    public Long getTimezoneId() { return timezoneId; }
    public void setTimezoneId(Long timezoneId) { this.timezoneId = timezoneId; }
}

