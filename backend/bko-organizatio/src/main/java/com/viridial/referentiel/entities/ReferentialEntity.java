package com.viridial.referentiel.entities;

import com.viridial.common.entities.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(
    name = "com_referential",
    indexes = {
        @Index(name = "idx_com_code", columnList = "code"),
        @Index(name = "idx_com_data_type", columnList = "data_type"),
        @Index(name = "idx_com_type_id", columnList = "type_id"),
        @Index(name = "idx_com_sub_type_id", columnList = "sub_type_id"),
        @Index(name = "idx_com_parent_id", columnList = "parent_id"),
        @Index(name = "idx_com_active", columnList = "active"),
        @Index(name = "idx_com_display_order", columnList = "data_type, display_order")
    }
)
public class ReferentialEntity extends BaseEntity {

    @Column(name = "code", nullable = false, length = 50)
    @NotBlank
    @Size(max = 50)
    private String code;

    @Column(name = "data_type", nullable = false, length = 50)
    @NotBlank
    @Size(max = 50)
    private String dataType;

    @Column(name = "label", nullable = false, length = 255)
    @NotBlank
    @Size(max = 255)
    private String label;

    @Column(name = "description", length = 500)
    @Size(max = 500)
    private String description;

    @Column(name = "external_code", length = 100)
    @Size(max = 100)
    private String externalCode;

    @Column(name = "icon_url", length = 500)
    @Size(max = 500)
    private String iconUrl;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "locale", length = 10)
    @Size(max = 10)
    private String locale;

    @Column(name = "type_id", nullable = false)
    @NotNull
    private Long typeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "type_id", nullable = false, insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_referential_type"))
    private ReferentialEntity type;

    @Column(name = "sub_type_id")
    private Long subTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_type_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_referential_sub_type"))
    private ReferentialEntity subType;

    @Column(name = "parent_id", nullable = true)
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true, insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_referential_parent"))
    private ReferentialEntity parent;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "start_date")
    private java.time.LocalDateTime startDate;

    @Column(name = "end_date")
    private java.time.LocalDateTime endDate;

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public ReferentialEntity getType() {
        return type;
    }

    public void setType(ReferentialEntity type) {
        this.type = type;
        this.typeId = type != null ? type.getId() : null;
    }

    public Long getSubTypeId() {
        return subTypeId;
    }

    public void setSubTypeId(Long subTypeId) {
        this.subTypeId = subTypeId;
    }

    public ReferentialEntity getSubType() {
        return subType;
    }

    public void setSubType(ReferentialEntity subType) {
        this.subType = subType;
        this.subTypeId = subType != null ? subType.getId() : null;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public ReferentialEntity getParent() {
        return parent;
    }

    public void setParent(ReferentialEntity parent) {
        this.parent = parent;
        this.parentId = parent != null ? parent.getId() : null;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public java.time.LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(java.time.LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public java.time.LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(java.time.LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
