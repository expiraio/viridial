package com.viridial.roles.resources;

import com.viridial.common.forms.BulkDeleteForm;
import com.viridial.common.forms.BulkDeleteResponse;
import com.viridial.common.forms.BulkUpdateActiveForm;
import com.viridial.common.forms.BulkUpdateActiveResponse;
import com.viridial.roles.forms.RoleForm;
import com.viridial.roles.forms.RoleSearchForm;
import com.viridial.roles.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleResource {
    @Autowired
    private RoleService roleService;

    @PostMapping("/search")
    public ResponseEntity<List<RoleForm>> search(@RequestBody RoleSearchForm search) {
        return ResponseEntity.ok(roleService.search(search));
    }

    @PostMapping("/bulk-update-active")
    public ResponseEntity<BulkUpdateActiveResponse> bulkUpdateActive(@RequestBody BulkUpdateActiveForm form) {
        int updatedCount = roleService.bulkUpdateActive(form.getIds(), form.isActive());
        return ResponseEntity.ok(new BulkUpdateActiveResponse(updatedCount, form.isActive()));
    }

    @PostMapping("/bulk-delete")
    public ResponseEntity<BulkDeleteResponse> bulkDelete(@RequestBody BulkDeleteForm form) {
        String deletedBy = null; // TODO: Get from security context
        int deletedCount = roleService.bulkDelete(form.getIds(), deletedBy);
        return ResponseEntity.ok(new BulkDeleteResponse(deletedCount));
    }
}

