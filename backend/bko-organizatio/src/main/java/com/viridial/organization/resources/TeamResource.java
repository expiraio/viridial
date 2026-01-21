package com.viridial.organization.resources;

import com.viridial.common.forms.BulkDeleteForm;
import com.viridial.common.forms.BulkDeleteResponse;
import com.viridial.common.forms.BulkUpdateActiveForm;
import com.viridial.common.forms.BulkUpdateActiveResponse;
import com.viridial.organization.forms.TeamForm;
import com.viridial.organization.forms.TeamSearchForm;
import com.viridial.organization.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamResource {
    @Autowired
    private TeamService teamService;

    @PostMapping("/search")
    public ResponseEntity<List<TeamForm>> search(@RequestBody TeamSearchForm search) {
        return ResponseEntity.ok(teamService.search(search));
    }

    @PostMapping("/bulk-update-active")
    public ResponseEntity<BulkUpdateActiveResponse> bulkUpdateActive(@RequestBody BulkUpdateActiveForm form) {
        int updatedCount = teamService.bulkUpdateActive(form.getIds(), form.isActive());
        return ResponseEntity.ok(new BulkUpdateActiveResponse(updatedCount, form.isActive()));
    }

    @PostMapping("/bulk-delete")
    public ResponseEntity<BulkDeleteResponse> bulkDelete(@RequestBody BulkDeleteForm form) {
        String deletedBy = null; // TODO: Get from security context
        int deletedCount = teamService.bulkDelete(form.getIds(), deletedBy);
        return ResponseEntity.ok(new BulkDeleteResponse(deletedCount));
    }
}

