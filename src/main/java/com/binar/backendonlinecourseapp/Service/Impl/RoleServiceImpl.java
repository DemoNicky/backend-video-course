package com.binar.backendonlinecourseapp.Service.Impl;

import com.binar.backendonlinecourseapp.Entity.Role;
import com.binar.backendonlinecourseapp.Repository.RoleRepository;
import com.binar.backendonlinecourseapp.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public String createRole(String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        roleRepository.save(role);
        return "berhasil";
    }

    @Override
    public String updateRole(Role role) {
        Role upRole = roleRepository.findById(role.getId())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        upRole.setRoleName(role.getRoleName());
        roleRepository.save(upRole);
        return "Success";
    }

    @Override
    public void deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        roleRepository.delete(role);
    }
}
