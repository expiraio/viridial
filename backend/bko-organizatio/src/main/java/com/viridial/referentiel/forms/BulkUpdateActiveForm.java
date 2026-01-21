package com.viridial.referentiel.forms;

import java.util.List;

/**
 * Form for bulk updating active status of referentials.
 */
public class BulkUpdateActiveForm {
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

