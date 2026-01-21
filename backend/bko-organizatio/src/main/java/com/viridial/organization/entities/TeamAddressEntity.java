package com.viridial.organization.entities;

import com.viridial.common.entities.BaseEntity;
import com.viridial.countries.entities.CityEntity;
import com.viridial.countries.entities.CountryEntity;
import com.viridial.referentiel.entities.ReferentialEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(
    name = "org_team_address",
    indexes = {
        @Index(name = "idx_team_addr_team_id", columnList = "team_id"),
        @Index(name = "idx_team_addr_type_id", columnList = "address_type_id"),
        @Index(name = "idx_team_addr_city_id", columnList = "city_id"),
        @Index(name = "idx_team_addr_country_id", columnList = "country_id"),
        @Index(name = "idx_team_addr_primary", columnList = "team_id, is_primary"),
        @Index(name = "idx_team_addr_validated", columnList = "validated")
    }
)
public class TeamAddressEntity extends BaseEntity {

    @Column(name = "street", length = 255)
    @Size(max = 255)
    private String street;

    @Column(name = "street2", length = 255)
    @Size(max = 255)
    private String street2;

    @Column(name = "street3", length = 255)
    @Size(max = 255)
    private String street3;

    @Column(name = "building", length = 100)
    @Size(max = 100)
    private String building;

    @Column(name = "floor", length = 20)
    @Size(max = 20)
    private String floor;

    @Column(name = "unit", length = 20)
    @Size(max = 20)
    private String unit;

    @Column(name = "postal_code", length = 20)
    @Size(max = 20)
    private String postalCode;

    @Column(name = "state", length = 100)
    @Size(max = 100)
    private String state;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "is_primary", nullable = false)
    private boolean primary = false;

    @Column(name = "validated", nullable = false)
    private boolean validated = false;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "team_id", nullable = false)
    @NotNull
    private Long teamId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false, insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_team_addr_team"))
    private TeamEntity team;

    @Column(name = "city_id")
    private Long cityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_team_addr_city"))
    private CityEntity city;

    @Column(name = "country_id", nullable = false)
    @NotNull
    private Long countryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id",  insertable = false, updatable = false, nullable = false,
                foreignKey = @ForeignKey(name = "fk_team_addr_country"), referencedColumnName = "id" )
    private CountryEntity country;

    @Column(name = "address_type_id", nullable = false)
    @NotNull
    private Long addressTypeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_type_id", insertable = false, updatable = false, nullable = false,
                foreignKey = @ForeignKey(name = "fk_team_addr_type"))
    private ReferentialEntity addressType;

    // Getters and Setters
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getStreet3() {
        return street3;
    }

    public void setStreet3(String street3) {
        this.street3 = street3;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public TeamEntity getTeam() {
        return team;
    }

    public void setTeam(TeamEntity team) {
        this.team = team;
        this.teamId = team != null ? team.getId() : null;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
        this.cityId = city != null ? city.getId() : null;
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

    public Long getAddressTypeId() {
        return addressTypeId;
    }

    public void setAddressTypeId(Long addressTypeId) {
        this.addressTypeId = addressTypeId;
    }

    public ReferentialEntity getAddressType() {
        return addressType;
    }

    public void setAddressType(ReferentialEntity addressType) {
        this.addressType = addressType;
        this.addressTypeId = addressType != null ? addressType.getId() : null;
    }
}

