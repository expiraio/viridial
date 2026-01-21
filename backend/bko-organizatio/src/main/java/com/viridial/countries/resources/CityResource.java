package com.viridial.countries.resources;

import com.viridial.common.forms.BulkDeleteForm;
import com.viridial.common.forms.BulkDeleteResponse;
import com.viridial.common.forms.BulkUpdateActiveForm;
import com.viridial.common.forms.BulkUpdateActiveResponse;
import com.viridial.countries.forms.CityForm;
import com.viridial.countries.forms.CitySearchForm;
import com.viridial.countries.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityResource {
    @Autowired
    private CityService cityService;

    @PostMapping("/search")
    public ResponseEntity<List<CityForm>> search(@RequestBody CitySearchForm search) {
        return ResponseEntity.ok(cityService.search(search));
    }

    @PostMapping("/bulk-update-active")
    public ResponseEntity<BulkUpdateActiveResponse> bulkUpdateActive(@RequestBody BulkUpdateActiveForm form) {
        int updatedCount = cityService.bulkUpdateActive(form.getIds(), form.isActive());
        return ResponseEntity.ok(new BulkUpdateActiveResponse(updatedCount, form.isActive()));
    }

    @PostMapping("/bulk-delete")
    public ResponseEntity<BulkDeleteResponse> bulkDelete(@RequestBody BulkDeleteForm form) {
        String deletedBy = null; // TODO: Get from security context
        int deletedCount = cityService.bulkDelete(form.getIds(), deletedBy);
        return ResponseEntity.ok(new BulkDeleteResponse(deletedCount));
    }
}

