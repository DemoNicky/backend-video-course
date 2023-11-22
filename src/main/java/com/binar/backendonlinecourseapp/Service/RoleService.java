package com.binar.backendonlinecourseapp.Service;

import com.binar.backendonlinecourseapp.Entity.Role;

public interface RoleService {
    String createRole(String roleName);

    String updateRole(Role role);

    void deleteRole(Long roleId);
}
