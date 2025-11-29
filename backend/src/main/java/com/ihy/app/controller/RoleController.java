package com.ihy.app.controller;

import com.ihy.app.common.constant.AppConstants;
import com.ihy.app.common.dto.response.ApiResponse;
import com.ihy.app.dto.request.RoleRequest;
import com.ihy.app.dto.response.RoleResponse;
import com.ihy.app.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping("/create")
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .code(AppConstants.SUCCESS_CODE)
                .build();
    }

    @GetMapping("")
    ApiResponse<List<RoleResponse>> getAllPermission(RoleRequest request) {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getALllRole())
                .code(AppConstants.SUCCESS_CODE)
                .build();
    }

    @DeleteMapping("/{roleName}")
    ApiResponse<Void> deleteRole(@PathVariable String roleName) {
        roleService.delete(roleName);
        return ApiResponse.<Void>builder()
                .code(AppConstants.SUCCESS_CODE)
                .message("Deleted role")
                .build( );
    }
}
