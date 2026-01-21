package com.viridial.countries.entities;

import com.viridial.common.entities.BaseEntity;
import com.viridial.referentiel.entities.ReferentialEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "com_countries",
    indexes = {
        @Index(name = "idx_country_iso2", columnList = "iso2"),
        @Index(name = "idx_country_iso3", columnList = "iso3"),
        @Index(name = "idx_country_numeric_code", columnList = "numeric_code"),
        @Index(name = "idx_country_active", columnList = "active"),
        @Index(name = "idx_country_capital_city_id", columnList = "capital_city_id"),
        @Index(name = "idx_country_currency_code", columnList = "currency_code"),
        @Index(name = "idx_country_timezone_id", columnList = "timezone_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_country_iso2", columnNames = "iso2"),
        @UniqueConstraint(name = "uk_country_iso3", columnNames = "iso3"),
        @UniqueConstraint(name = "uk_country_numeric_code", columnNames = "numeric_code")
    }
)
public class CountryEntity extends BaseEntity {

    @Column(name = "name", nullable = false, length = 255)
    @NotBlank
    @Size(max = 255)
    private String name;

    @Column(name = "native_name", nullable = false, length = 255)
    @NotBlank
    @Size(max = 255)
    private String nativeName;

    @Column(name = "iso2", nullable = false, length = 2)
    @NotBlank
    @Size(min = 2, max = 2)
    private String iso2;

    @Column(name = "iso3", nullable = false, length = 3)
    @NotBlank
    @Size(min = 3, max = 3)
    private String iso3;

    @Column(name = "numeric_code", nullable = false, length = 3)
    @NotBlank
    @Size(min = 3, max = 3)
    private String numericCode;

    @Column(name = "region_id")
    private Long regionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_country_region"))
    private ReferentialEntity region;

    @Column(name = "sub_region_id")
    private Long subRegionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_region_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_country_sub_region"))
    private ReferentialEntity subRegion;

    @Column(name = "language_id", nullable = false)
    @NotNull
    private Long languageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "language_id", nullable = false, insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_country_language"))
    private ReferentialEntity language;

    @Column(name = "phone_code", length = 5)
    @Size(max = 5)
    private String phoneCode;

    @Column(name = "timezone_id")
    private Long timezoneId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timezone_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_country_timezone"))
    private TimezoneEntity timezone;

    @Column(name = "currency_code", length = 3)
    @Size(max = 3)
    private String currencyCode;

    @Column(name = "domain", length = 10)
    @Size(max = 10)
    private String domain;

    @Column(name = "flag_emoji", length = 10)
    @Size(max = 10)
    private String flagEmoji;

    @Column(name = "capital_city_id")
    private Long capitalCityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capital_city_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_country_capital_city"))
    private CityEntity capitalCity;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CityEntity> cities = new ArrayList<>();

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getNumericCode() {
        return numericCode;
    }

    public void setNumericCode(String numericCode) {
        this.numericCode = numericCode;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public ReferentialEntity getRegion() {
        return region;
    }

    public void setRegion(ReferentialEntity region) {
        this.region = region;
        this.regionId = region != null ? region.getId() : null;
    }

    public Long getSubRegionId() {
        return subRegionId;
    }

    public void setSubRegionId(Long subRegionId) {
        this.subRegionId = subRegionId;
    }

    public ReferentialEntity getSubRegion() {
        return subRegion;
    }

    public void setSubRegion(ReferentialEntity subRegion) {
        this.subRegion = subRegion;
        this.subRegionId = subRegion != null ? subRegion.getId() : null;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    public ReferentialEntity getLanguage() {
        return language;
    }

    public void setLanguage(ReferentialEntity language) {
        this.language = language;
        this.languageId = language != null ? language.getId() : null;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public Long getTimezoneId() {
        return timezoneId;
    }

    public void setTimezoneId(Long timezoneId) {
        this.timezoneId = timezoneId;
    }

    public TimezoneEntity getTimezone() {
        return timezone;
    }

    public void setTimezone(TimezoneEntity timezone) {
        this.timezone = timezone;
        this.timezoneId = timezone != null ? timezone.getId() : null;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getFlagEmoji() {
        return flagEmoji;
    }

    public void setFlagEmoji(String flagEmoji) {
        this.flagEmoji = flagEmoji;
    }

    public Long getCapitalCityId() {
        return capitalCityId;
    }

    public void setCapitalCityId(Long capitalCityId) {
        this.capitalCityId = capitalCityId;
    }

    public CityEntity getCapitalCity() {
        return capitalCity;
    }

    public void setCapitalCity(CityEntity capitalCity) {
        this.capitalCity = capitalCity;
        this.capitalCityId = capitalCity != null ? capitalCity.getId() : null;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<CityEntity> getCities() {
        return cities;
    }

    public void setCities(List<CityEntity> cities) {
        this.cities = cities;
    }
}
