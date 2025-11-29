package com.ihy.app.service;

import com.ihy.app.dto.request.PermissionRequest;
import com.ihy.app.dto.response.PermissionResponse;
import com.ihy.app.entity.Permission;
import com.ihy.app.mapper.PermissionMapper;
import com.ihy.app.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {

    PermissionRepository permissionRepository;

    PermissionMapper permissionMapper;


    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAllPermission(){
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permission ->  permissionMapper.toPermissionResponse(permission)).toList();
    }

    public void delete(String permissionName){
        permissionRepository.deleteById(permissionName);
    }
}
