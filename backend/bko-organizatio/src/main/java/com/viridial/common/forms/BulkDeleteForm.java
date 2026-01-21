package com.viridial.common.forms;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Generic form for bulk delete operations.
 */
public class BulkDeleteForm {
    @NotEmpty(message = "{validation.ids.notEmpty}")
    private List<Long> ids;

    public BulkDeleteForm() {
    }

    public BulkDeleteForm(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}

