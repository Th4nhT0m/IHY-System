package com.ihy.app.mapper;

import com.ihy.app.dto.request.RoleRequest;
import com.ihy.app.dto.response.RoleResponse;
import com.ihy.app.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    //The "Permissions" attribute in RoleRequest is different from Role
    @Mapping(target = "permission", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
