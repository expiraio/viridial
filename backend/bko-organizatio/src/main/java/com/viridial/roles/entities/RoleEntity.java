package com.viridial.roles.entities;

import com.viridial.common.entities.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "sec_role",
    indexes = {
        @Index(name = "idx_sec_role_code", columnList = "code"),
        @Index(name = "idx_sec_role_active", columnList = "active"),
        @Index(name = "idx_sec_role_admin", columnList = "admin"),
        @Index(name = "idx_sec_role_parent_id", columnList = "parent_id")
    }
)
public class RoleEntity extends BaseEntity {

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
    
    @Column(name = "admin", nullable = false)
    private boolean admin = false;

    @Column(name = "parent_id", nullable = true)
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_feature_parent"))
    private RoleEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoleEntity> children = new ArrayList<>();

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

    public boolean isAdmin() {
        return admin;
    }   
    
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public RoleEntity getParent() {
        return parent;
    }
    
    public void setParent(RoleEntity parent) {
        this.parent = parent;
    }

    public List<RoleEntity> getChildren() {
        return children;
    }
    
    public void setChildren(List<RoleEntity> children) {
        this.children = children;
    }   
    public void addChild(RoleEntity child) {
        children.add(child);
        child.setParent(this);
    }
    public void removeChild(RoleEntity child) {
        children.remove(child);
        child.setParent(null);
    }

    public void clearChildren() {
        children.clear();
    }
    public void clearParent() {
        parent = null;
    }
    public void clearAll() {
        clearChildren();
        clearParent();
    }


}

