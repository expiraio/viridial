package com.viridial.organization.entities;

import com.viridial.common.entities.BaseEntity;
import com.viridial.referentiel.entities.ReferentialEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "org_team",
    indexes = {
        @Index(name = "idx_team_internal_code", columnList = "internal_code"),
        @Index(name = "idx_team_parent_id", columnList = "parent_id"),
        @Index(name = "idx_team_active", columnList = "active"),
        @Index(name = "idx_team_type_id", columnList = "team_type_id"),
        @Index(name = "idx_team_industry_id", columnList = "industry_id"),
        @Index(name = "idx_team_email", columnList = "email")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_team_internal_code", columnNames = "internal_code")
    }
)
public class TeamEntity extends BaseEntity {

    @Column(name = "internal_code", nullable = false, unique = true, length = 50)
    @NotBlank
    @Size(max = 50)
    private String internalCode;

    @Column(name = "external_code", length = 100)
    @Size(max = 100)
    private String externalCode;

    @Column(name = "name", nullable = false, length = 255)
    @NotBlank
    @Size(max = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "email", length = 255)
    @Email
    @Size(max = 255)
    private String email;

    @Column(name = "website", length = 500)
    @Size(max = 500)
    private String website;

    @Column(name = "tax_id", length = 50)
    @Size(max = 50)
    private String taxId;

    @Column(name = "vat_number", length = 50)
    @Size(max = 50)
    private String vatNumber;

    @Column(name = "logo_url", length = 500)
    @Size(max = 500)
    private String logoUrl;

    @Column(name = "founded_date")
    @PastOrPresent
    private LocalDate foundedDate;

    @Column(name = "employee_count")
    private Integer employeeCount;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "parent_id")
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_team_parent"))
    private TeamEntity parent;

    @Column(name = "team_type_id")
    private Long teamTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_type_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_team_type"))
    private ReferentialEntity teamType;

    @Column(name = "industry_id")
    private Long industryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_team_industry"))
    private ReferentialEntity industry;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamEntity> children = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamAddressEntity> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamPhoneEntity> phones = new ArrayList<>();

    // Getters and Setters
    public String getInternalCode() {
        return internalCode;
    }

    public void setInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public LocalDate getFoundedDate() {
        return foundedDate;
    }

    public void setFoundedDate(LocalDate foundedDate) {
        this.foundedDate = foundedDate;
    }

    public Integer getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public TeamEntity getParent() {
        return parent;
    }

    public void setParent(TeamEntity parent) {
        this.parent = parent;
        if (parent != null) {
            this.parentId = parent.getId();
        } else {
            this.parentId = null;
        }
    }

    public Long getTeamTypeId() {
        return teamTypeId;
    }

    public void setTeamTypeId(Long teamTypeId) {
        this.teamTypeId = teamTypeId;
    }

    public ReferentialEntity getTeamType() {
        return teamType;
    }

    public void setTeamType(ReferentialEntity teamType) {
        this.teamType = teamType;
        this.teamTypeId = teamType != null ? teamType.getId() : null;
    }

    public Long getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Long industryId) {
        this.industryId = industryId;
    }

    public ReferentialEntity getIndustry() {
        return industry;
    }

    public void setIndustry(ReferentialEntity industry) {
        this.industry = industry;
        this.industryId = industry != null ? industry.getId() : null;
    }

    public List<TeamEntity> getChildren() {
        return children;
    }

    public void setChildren(List<TeamEntity> children) {
        this.children = children;
    }

    public List<TeamAddressEntity> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<TeamAddressEntity> addresses) {
        this.addresses = addresses;
    }

    public List<TeamPhoneEntity> getPhones() {
        return phones;
    }

    public void setPhones(List<TeamPhoneEntity> phones) {
        this.phones = phones;
    }

}

