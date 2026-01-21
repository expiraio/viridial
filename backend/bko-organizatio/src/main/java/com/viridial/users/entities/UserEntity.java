package com.viridial.users.entities;

import java.time.LocalDateTime;

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import com.viridial.common.entities.BaseEntity;
import com.viridial.referentiel.entities.ReferentialEntity;

@Entity
@Table(
    name = "sec_user"
)
public class UserEntity extends BaseEntity {
    
    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 255)
    private String lastName;
    @Column(name = "email", nullable = false, length = 255)
    private String email;
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "status_id", nullable = true)
    private Long statusId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = true, insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_user_status"))
    private ReferentialEntity status;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "last_login_at", nullable = true)
    private LocalDateTime lastLoginAt;
    @Column(name = "last_login_by", nullable = true)
    private String lastLoginBy;
    @Column(name = "last_login_ip", nullable = true)
    private String lastLoginIp;
    @Column(name = "last_login_user_agent", nullable = true)
    private String lastLoginUserAgent;
    @Column(name = "last_login_device", nullable = true)
    private String lastLoginDevice;
    @Column(name = "last_login_location", nullable = true)
    private String lastLoginLocation;
    @Column(name = "last_login_browser", nullable = true)
    private String lastLoginBrowser;
    @Column(name = "last_login_os", nullable = true)
    private String lastLoginOs;

    @Column(name = "active_at", nullable = true)
    private LocalDateTime activeAt;
    @Column(name = "inactive_at", nullable = true)
    private LocalDateTime inactiveAt;
    @Column(name = "active_by", nullable = true)
    private String activeBy;
    @Column(name = "inactive_by", nullable = true)
    private String inactiveBy;

     // getters and setters
     public String getFirstName() {
        return firstName;
     }
     public void setFirstName(String firstName) {
        this.firstName = firstName;
     }
     public String getLastName() {
        return lastName;
     }
     public void setLastName(String lastName) {
        this.lastName = lastName;
     }
     public String getEmail() {
        return email;
     }
     public void setEmail(String email) {
        this.email = email;
     }
     public String getPassword() {
        return password;
     }
     public void setPassword(String password) {
        this.password = password;
     }
     public Long getStatusId() {
        return statusId;
     }
     public void setStatusId(Long statusId) {
        this.statusId = statusId;
     }
     public ReferentialEntity getStatus() {
        return status;
     }
     public void setStatus(ReferentialEntity status) {
        this.status = status;
     }
     public boolean isActive() {
        return active;
     }
     public void setActive(boolean active) {
        this.active = active;
     }
     public LocalDateTime getActiveAt() {
        return activeAt;
     }
     public void setActiveAt(LocalDateTime activeAt) {
        this.activeAt = activeAt;
     }
     public LocalDateTime getInactiveAt() {
        return inactiveAt;
     }
     public void setInactiveAt(LocalDateTime inactiveAt) {
        this.inactiveAt = inactiveAt;
     }
     public String getActiveBy() {
        return activeBy;
     }
     public void setActiveBy(String activeBy) {
        this.activeBy = activeBy;
     }
     public String getInactiveBy() {
        return inactiveBy;
     }
     public void setInactiveBy(String inactiveBy) {
        this.inactiveBy = inactiveBy;
     }
     public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
     }
     public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
     }
     public String getLastLoginBy() {
        return lastLoginBy;
     }
     public void setLastLoginBy(String lastLoginBy) {
        this.lastLoginBy = lastLoginBy;
     }
     public String getLastLoginIp() {
        return lastLoginIp;
     }
     public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
     }
     public String getLastLoginUserAgent() {
        return lastLoginUserAgent;
     }
     public void setLastLoginUserAgent(String lastLoginUserAgent) {
        this.lastLoginUserAgent = lastLoginUserAgent;
     }
     public String getLastLoginDevice() {
        return lastLoginDevice;
     }
     public void setLastLoginDevice(String lastLoginDevice) {
        this.lastLoginDevice = lastLoginDevice;
     }
     public String getLastLoginLocation() {
        return lastLoginLocation;
     }
     public void setLastLoginLocation(String lastLoginLocation) {
        this.lastLoginLocation = lastLoginLocation;
     }
     public String getLastLoginBrowser() {
        return lastLoginBrowser;
     }
     public void setLastLoginBrowser(String lastLoginBrowser) {
        this.lastLoginBrowser = lastLoginBrowser;
     }
     public String getLastLoginOs() {
        return lastLoginOs;
     }
     public void setLastLoginOs(String lastLoginOs) {
        this.lastLoginOs = lastLoginOs;
     }
     public void clearStatus() {
        this.status = null;
     }
     public void clearAll() {
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.password = null;
        this.statusId = null;
        this.status = null;
        this.active = true;
     }
     public boolean isDeleted() {
        return getDeletedAt() != null;
     }
     public void delete(String deletedBy) {
        this.setDeletedAt(LocalDateTime.now());
        this.setDeletedBy(deletedBy);
     }  
     public void restore() {
        this.setDeletedAt(null);
        this.setDeletedBy(null);
     }
}   