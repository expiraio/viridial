package com.viridial.countries.forms;

import java.time.LocalDateTime;

/**
 * Form DTO for CountryEntity.
 */
public class CountryForm {
    private Long id;
    private String name;
    private String nativeName;
    private String iso2;
    private String iso3;
    private String numericCode;
    private String phoneCode;
    private String currencyCode;
    private String domain;
    private String flagEmoji;
    private Long regionId;
    private Long subRegionId;
    private Long languageId;
    private Long timezoneId;
    private Long capitalCityId;
    private boolean enabled;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNativeName() { return nativeName; }
    public void setNativeName(String nativeName) { this.nativeName = nativeName; }
    public String getIso2() { return iso2; }
    public void setIso2(String iso2) { this.iso2 = iso2; }
    public String getIso3() { return iso3; }
    public void setIso3(String iso3) { this.iso3 = iso3; }
    public String getNumericCode() { return numericCode; }
    public void setNumericCode(String numericCode) { this.numericCode = numericCode; }
    public String getPhoneCode() { return phoneCode; }
    public void setPhoneCode(String phoneCode) { this.phoneCode = phoneCode; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getFlagEmoji() { return flagEmoji; }
    public void setFlagEmoji(String flagEmoji) { this.flagEmoji = flagEmoji; }
    public Long getRegionId() { return regionId; }
    public void setRegionId(Long regionId) { this.regionId = regionId; }
    public Long getSubRegionId() { return subRegionId; }
    public void setSubRegionId(Long subRegionId) { this.subRegionId = subRegionId; }
    public Long getLanguageId() { return languageId; }
    public void setLanguageId(Long languageId) { this.languageId = languageId; }
    public Long getTimezoneId() { return timezoneId; }
    public void setTimezoneId(Long timezoneId) { this.timezoneId = timezoneId; }
    public Long getCapitalCityId() { return capitalCityId; }
    public void setCapitalCityId(Long capitalCityId) { this.capitalCityId = capitalCityId; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

