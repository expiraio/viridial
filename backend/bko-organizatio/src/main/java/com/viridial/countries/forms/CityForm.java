package com.viridial.countries.forms;

import java.time.LocalDateTime;

/**
 * Form DTO for CityEntity.
 */
public class CityForm {
    private Long id;
    private String name;
    private String nativeName;
    private String state;
    private String district;
    private String postalCode;
    private Double latitude;
    private Double longitude;
    private Double elevation;
    private Long population;
    private Double areaKm2;
    private Long timezoneId;
    private Long countryId;
    private boolean capital;
    private boolean metropolitan;
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
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getElevation() { return elevation; }
    public void setElevation(Double elevation) { this.elevation = elevation; }
    public Long getPopulation() { return population; }
    public void setPopulation(Long population) { this.population = population; }
    public Double getAreaKm2() { return areaKm2; }
    public void setAreaKm2(Double areaKm2) { this.areaKm2 = areaKm2; }
    public Long getTimezoneId() { return timezoneId; }
    public void setTimezoneId(Long timezoneId) { this.timezoneId = timezoneId; }
    public Long getCountryId() { return countryId; }
    public void setCountryId(Long countryId) { this.countryId = countryId; }
    public boolean isCapital() { return capital; }
    public void setCapital(boolean capital) { this.capital = capital; }
    public boolean isMetropolitan() { return metropolitan; }
    public void setMetropolitan(boolean metropolitan) { this.metropolitan = metropolitan; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

