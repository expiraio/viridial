package com.viridial.referentiel.forms;

import java.time.LocalDateTime;

import com.viridial.common.form.BaseForm;

/**
 * Form DTO for ReferentialEntity operations (create/update).
 */
public class ReferentialForm extends BaseForm{

    private String code;

    private String dataType;

    private String label;

    private String description;

    private String externalCode;

    private String iconUrl;

    private Integer displayOrder;

    private String locale;

    private Long typeId;

    private String typeDataType;
    
    private String typeLabel;

    private String typeDescription;

    private String typeCode;

    private Long subTypeId;

    private String subTypeDataType;

    private String subTypeLabel;

    private String subTypeDescription;

    private String subTypeCode;

    private Long parentId;

    private String parentDataType;

    private String parentLabel;

    private String parentDescription;

    private String parentCode;

    private boolean active = true;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

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

    public String getTypeDataType() {
        return typeDataType;
    }

    public void setTypeDataType(String typeDataType) {
        this.typeDataType = typeDataType;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Long getSubTypeId() {
        return subTypeId;
    }

    public void setSubTypeId(Long subTypeId) {
        this.subTypeId = subTypeId;
    }

    public String getSubTypeDataType() {
        return subTypeDataType;
    }

    public void setSubTypeDataType(String subTypeDataType) {
        this.subTypeDataType = subTypeDataType;
    }

    public String getSubTypeLabel() {
        return subTypeLabel;
    }

    public void setSubTypeLabel(String subTypeLabel) {
        this.subTypeLabel = subTypeLabel;
    }

    public String getSubTypeDescription() {
        return subTypeDescription;
    }

    public void setSubTypeDescription(String subTypeDescription) {
        this.subTypeDescription = subTypeDescription;
    }

    public String getSubTypeCode() {
        return subTypeCode;
    }

    public void setSubTypeCode(String subTypeCode) {
        this.subTypeCode = subTypeCode;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentDataType() {
        return parentDataType;
    }

    public void setParentDataType(String parentDataType) {
        this.parentDataType = parentDataType;
    }

    public String getParentLabel() {
        return parentLabel;
    }

    public void setParentLabel(String parentLabel) {
        this.parentLabel = parentLabel;
    }

    public String getParentDescription() {
        return parentDescription;
    }

    public void setParentDescription(String parentDescription) {
        this.parentDescription = parentDescription;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    
    
}

