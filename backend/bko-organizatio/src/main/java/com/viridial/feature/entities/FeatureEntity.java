package com.viridial.feature.entities;

import com.viridial.common.entities.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "feat_feature",
    indexes = {
        @Index(name = "idx_feat_feature_code", columnList = "code"),
        @Index(name = "idx_feat_feature_active", columnList = "active"),
        @Index(name = "idx_feat_feature_type_id", columnList = "type_id"),
        @Index(name = "idx_feat_feature_parent_id", columnList = "parent_id")
    }
)
public class FeatureEntity extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true, length = 50)
    @NotBlank
    @Size(max = 50)
    private String code;

    @Column(name = "label", nullable = false, length = 255)         
    @NotBlank
    @Size(max = 255)
    private String label;

    @Column(name = "description", columnDefinition = "TEXT")    
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active = true;
    
    @Column(name = "display_order", nullable = false)
    private int displayOrder = 0;

    
    @Column(name = "type_id", nullable = false)
    private Long typeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_feature_type"))
    private FeatureTypeEntity type;

    @Column(name = "parent_id", nullable = true)
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_feature_parent"))
    private FeatureEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeatureEntity> children = new ArrayList<>();

    // getters and setters
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
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
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public int getDisplayOrder() {
        return displayOrder;
    }
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
    public Long getTypeId() {
        return typeId;
    }
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
    public FeatureTypeEntity getType() {
        return type;
    }
    public void setType(FeatureTypeEntity type) {
        this.type = type;
    }
    public Long getParentId() {
        return parentId;
    } 
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    public FeatureEntity getParent() {
        return parent;
    }
    public void setParent(FeatureEntity parent) {
        this.parent = parent;
    }
    public List<FeatureEntity> getChildren() {
        return children;
    }
    public void setChildren(List<FeatureEntity> children) {
        this.children = children;
    }
}

