package com.ihy.app.mapper;


import com.ihy.app.dto.request.UserCreateRequest;
import com.ihy.app.dto.request.UserUpdateRequest;
import com.ihy.app.dto.response.UserResponse;
import com.ihy.app.entity.Users;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    @Mapping(target = "isActive", constant = "1")
    Users toCreateUser(UserCreateRequest request);

    @Mapping(target = "password", ignore = true)
    void toUpdateUser(@MappingTarget Users users, UserUpdateRequest request);

    UserResponse toUserResponse(Users users);

    List<UserResponse> toResponseUserList(List<Users> users);
}
