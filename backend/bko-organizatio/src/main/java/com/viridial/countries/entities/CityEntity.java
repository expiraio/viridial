package com.viridial.countries.entities;

import com.viridial.common.entities.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(
    name = "com_cities",
    indexes = {
        @Index(name = "idx_city_country_id", columnList = "country_id"),
        @Index(name = "idx_city_name", columnList = "name"),
        @Index(name = "idx_city_state", columnList = "state"),
        @Index(name = "idx_city_postal_code", columnList = "postal_code"),
        @Index(name = "idx_city_active", columnList = "active"),
        @Index(name = "idx_city_capital", columnList = "capital"),
        @Index(name = "idx_city_timezone_id", columnList = "timezone_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_city_unique",
            columnNames = {"name", "country_id", "state"}
        )
    }
)
public class CityEntity extends BaseEntity {

    @Column(name = "name", nullable = false, length = 255)
    @NotBlank
    @Size(max = 255)
    private String name;

    @Column(name = "native_name", length = 255)
    @Size(max = 255)
    private String nativeName;

    @Column(name = "state", length = 100)
    @Size(max = 100)
    private String state;

    @Column(name = "district", length = 100)
    @Size(max = 100)
    private String district;

    @Column(name = "postal_code", length = 20)
    @Size(max = 20)
    private String postalCode;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "elevation")
    private Double elevation;

    @Column(name = "population")
    private Long population;

    @Column(name = "area_km2")
    private Double areaKm2;

    @Column(name = "timezone_id")
    private Long timezoneId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timezone_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_city_timezone"))
    private TimezoneEntity timezone;

    @Column(name = "country_id", nullable = false)
    @NotNull
    private Long countryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false, insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_city_country"))
    private CountryEntity country;

    @Column(name = "capital", nullable = false)
    private boolean capital = false;

    @Column(name = "metropolitan", nullable = false)
    private boolean metropolitan = false;

    @Column(name = "active", nullable = false)
    private boolean active = true;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getElevation() {
        return elevation;
    }

    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public Double getAreaKm2() {
        return areaKm2;
    }

    public void setAreaKm2(Double areaKm2) {
        this.areaKm2 = areaKm2;
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

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
        this.countryId = country != null ? country.getId() : null;
    }

    public boolean isCapital() {
        return capital;
    }

    public void setCapital(boolean capital) {
        this.capital = capital;
    }

    public boolean isMetropolitan() {
        return metropolitan;
    }

    public void setMetropolitan(boolean metropolitan) {
        this.metropolitan = metropolitan;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
