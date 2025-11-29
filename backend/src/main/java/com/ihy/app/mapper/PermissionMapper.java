package com.ihy.app.mapper;

import com.ihy.app.dto.request.PermissionRequest;
import com.ihy.app.dto.response.PermissionResponse;
import com.ihy.app.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
