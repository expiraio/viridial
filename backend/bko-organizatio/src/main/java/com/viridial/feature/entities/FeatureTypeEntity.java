package com.viridial.feature.entities;

import com.viridial.common.entities.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(
    name = "feat_feature_type",
    indexes = {
        @Index(name = "idx_feat_feature_type_code", columnList = "code")
    }
)
public class FeatureTypeEntity extends BaseEntity {

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
}
