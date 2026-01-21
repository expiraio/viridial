package com.viridial.referentiel.resources;

import com.viridial.common.forms.PaginatedResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viridial.common.forms.BulkDeleteForm;
import com.viridial.common.forms.BulkDeleteResponse;
import com.viridial.common.forms.BulkUpdateActiveForm;
import com.viridial.common.forms.BulkUpdateActiveResponse;
import com.viridial.referentiel.forms.FormSearch;
import com.viridial.referentiel.forms.ReferentialForm;
import com.viridial.referentiel.services.ReferentialSearchService;
import com.viridial.referentiel.services.ReferentialUpdateService;

@RestController
@RequestMapping("/referentiels")
public class ReferentialResource {
    @Autowired
    private ReferentialSearchService referentialSearchService;
    @Autowired
    private ReferentialUpdateService referentialUpdateService;

    @PostMapping("/search")
    public ResponseEntity<PaginatedResponse<ReferentialForm>> search(@RequestBody FormSearch search) {
        return ResponseEntity.ok(referentialSearchService.search(search));
    }

    @PostMapping("/bulk-update-active")
    public ResponseEntity<BulkUpdateActiveResponse> bulkUpdateActive(@RequestBody BulkUpdateActiveForm form) {
        int updatedCount = referentialUpdateService.bulkUpdateActive(form.getIds(), form.isActive());
        return ResponseEntity.ok(new BulkUpdateActiveResponse(updatedCount, form.isActive()));
    }

    @PostMapping("/bulk-delete")
    public ResponseEntity<BulkDeleteResponse> bulkDelete(@RequestBody BulkDeleteForm form) {
        // TODO: Get current user ID from security context if needed
        String deletedBy = "1"; // Can be set from security context: SecurityContextHolder.getContext().getAuthentication()
        int deletedCount = referentialUpdateService.bulkDelete(form.getIds(), deletedBy);
        return ResponseEntity.ok(new BulkDeleteResponse(deletedCount));
    }

}
