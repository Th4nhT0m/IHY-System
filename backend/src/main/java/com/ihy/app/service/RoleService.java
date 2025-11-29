package com.ihy.app.service;


import com.ihy.app.dto.request.RoleRequest;
import com.ihy.app.dto.response.RoleResponse;
import com.ihy.app.mapper.RoleMapper;
import com.ihy.app.repository.PermissionRepository;
import com.ihy.app.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {

    RoleRepository roleRepository;

    RoleMapper roleMapper;

    PermissionRepository permissionRepository;


    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermission());
        role.setPermission(new HashSet<>(permissions));

        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);

    }

    public List<RoleResponse> getALllRole() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String roleName){
        roleRepository.deleteById(roleName);
    }
}
