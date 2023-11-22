package com.binar.backendonlinecourseapp.Controller;

import com.binar.backendonlinecourseapp.Entity.Role;
import com.binar.backendonlinecourseapp.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> createRole(@RequestParam String roleName){
        String response = roleService.createRole(roleName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateRole(@RequestBody Role role) {
        String response = roleService.updateRole(role);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @DeleteMapping("/delete/{roleId}")
    public ResponseEntity<String> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.ok("Role deleted successfully");
    }

}
