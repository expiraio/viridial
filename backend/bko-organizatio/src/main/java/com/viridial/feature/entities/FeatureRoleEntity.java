package com.viridial.feature.entities;

import com.viridial.common.entities.BaseEntity;
import com.viridial.roles.entities.RoleEntity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "feat_feature_role",
    indexes = {
        @Index(name = "idx_feat_feature_role_feature_id", columnList = "feature_id"),
        @Index(name = "idx_feat_feature_role_role_id", columnList = "role_id"),
        @Index(name = "idx_feat_feature_role_active", columnList = "active")
    }
)
public class FeatureRoleEntity extends BaseEntity {

    @Column(name = "feature_id", nullable = true)
    private Long featureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_feature"))
    private FeatureEntity feature;

    @Column(name = "role_id", nullable = true)
    private Long roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_role"))
    private RoleEntity role;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    
    // getters and setters
    public Long getFeatureId() {
        return featureId;
    }
    
    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }
    
    public FeatureEntity getFeature() {
        return feature;
    }
    
    public void setFeature(FeatureEntity feature) {
        this.feature = feature;
    }
    
    public Long getRoleId() {
        return roleId;
    }   

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    public RoleEntity getRole() {
        return role;
    }   

    public void setRole(RoleEntity role) {
        this.role = role;
    }
    
    public boolean isActive() {
        return active;
    }   

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void clearFeature() {
        feature = null;
    }   

    public void clearRole() {
        role = null;
    }   
    
    public void clearAll() {
        clearFeature();
        clearRole();
    }   
}

