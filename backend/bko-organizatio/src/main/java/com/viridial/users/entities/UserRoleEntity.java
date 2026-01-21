package com.viridial.users.entities;

import java.time.LocalDateTime;

import com.viridial.common.entities.BaseEntity;
import com.viridial.roles.entities.RoleEntity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "sec_user_role",
    indexes = {
        @Index(name = "idx_sec_user_role_user_id", columnList = "user_id"),
        @Index(name = "idx_sec_user_role_role_id", columnList = "role_id"),
        @Index(name = "idx_sec_user_role_active", columnList = "active")
    }
)
public class UserRoleEntity extends BaseEntity {

    @Column(name = "user_id", nullable = true)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_user"))
    private UserEntity user;

    @Column(name = "role_id", nullable = true)
    private Long roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_role"))
    private RoleEntity role;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "start_date", nullable = false)
    private  LocalDateTime startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;
    
    // getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
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

    public void clearUser() {
        this.user = null;
    }

    public void clearRole() {
        this.role = null;
    }
    
    public void clearAll() {
        this.user = null;
        this.role = null;
    }   

    public boolean isDeleted() {
        return getDeletedAt() != null;
    }

    public void delete(String deletedBy) {
        this.setDeletedAt(LocalDateTime.now());
        this.setDeletedBy(deletedBy);
    }
    
}

