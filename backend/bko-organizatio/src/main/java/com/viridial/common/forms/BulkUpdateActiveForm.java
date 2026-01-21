package com.viridial.common.forms;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Generic form for bulk updating active status.
 */
public class BulkUpdateActiveForm {
    @NotEmpty(message = "{validation.ids.notEmpty}")
    private List<Long> ids;
    private boolean active;

    public BulkUpdateActiveForm() {
    }

    public BulkUpdateActiveForm(List<Long> ids, boolean active) {
        this.ids = ids;
        this.active = active;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

