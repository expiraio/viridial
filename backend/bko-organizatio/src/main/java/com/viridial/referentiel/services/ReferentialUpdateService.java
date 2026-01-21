package com.viridial.referentiel.services;

import java.util.List;

public interface ReferentialUpdateService {
    /**
     * Bulk update active status for referentials.
     * @param ids List of referential IDs to update
     * @param active The active status to set
     * @return Number of updated records
     */
    int bulkUpdateActive(List<Long> ids, boolean active);

    /**
     * Bulk delete referentials (logical delete).
     * @param ids List of referential IDs to delete
     * @param deletedBy The email of the user performing the deletion (optional, can be null)
     * @return Number of deleted records
     */
    int bulkDelete(List<Long> ids, String deletedBy);
}