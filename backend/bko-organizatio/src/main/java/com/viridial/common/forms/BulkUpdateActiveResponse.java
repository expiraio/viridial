package com.viridial.common.forms;

/**
 * Generic response DTO for bulk update active operation.
 */
public class BulkUpdateActiveResponse {
    private int updatedCount;
    private boolean active;

    public BulkUpdateActiveResponse() {
    }

    public BulkUpdateActiveResponse(int updatedCount, boolean active) {
        this.updatedCount = updatedCount;
        this.active = active;
    }

    public int getUpdatedCount() {
        return updatedCount;
    }

    public void setUpdatedCount(int updatedCount) {
        this.updatedCount = updatedCount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

