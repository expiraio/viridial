package com.viridial.users.resources;

import com.viridial.common.forms.BulkDeleteForm;
import com.viridial.common.forms.BulkDeleteResponse;
import com.viridial.common.forms.BulkUpdateActiveForm;
import com.viridial.common.forms.BulkUpdateActiveResponse;
import com.viridial.users.forms.UserForm;
import com.viridial.users.forms.UserSearchForm;
import com.viridial.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserResource {
    @Autowired
    private UserService userService;

    @PostMapping("/search")
    public ResponseEntity<List<UserForm>> search(@RequestBody UserSearchForm search) {
        return ResponseEntity.ok(userService.search(search));
    }

    @PostMapping("/bulk-update-active")
    public ResponseEntity<BulkUpdateActiveResponse> bulkUpdateActive(@RequestBody BulkUpdateActiveForm form) {
        int updatedCount = userService.bulkUpdateActive(form.getIds(), form.isActive());
        return ResponseEntity.ok(new BulkUpdateActiveResponse(updatedCount, form.isActive()));
    }

    @PostMapping("/bulk-delete")
    public ResponseEntity<BulkDeleteResponse> bulkDelete(@RequestBody BulkDeleteForm form) {
        String deletedBy = null; // TODO: Get from security context
        int deletedCount = userService.bulkDelete(form.getIds(), deletedBy);
        return ResponseEntity.ok(new BulkDeleteResponse(deletedCount));
    }
}

