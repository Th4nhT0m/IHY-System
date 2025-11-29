package com.ihy.app.controller;

import com.ihy.app.common.constant.AppConstants;
import com.ihy.app.common.dto.response.ApiResponse;
import com.ihy.app.dto.request.PermissionRequest;
import com.ihy.app.dto.response.PermissionResponse;
import com.ihy.app.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping("/create")
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .code(AppConstants.SUCCESS_CODE)
                .build();
    }

    @GetMapping("")
    ApiResponse<List<PermissionResponse>> getAllPermission(PermissionRequest request) {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAllPermission())
                .code(AppConstants.SUCCESS_CODE)
                .build();
    }

    @DeleteMapping("{permissionName}")
    ApiResponse<Void> deleteRole(@PathVariable String permissionName) {
        permissionService.delete(permissionName);
        return ApiResponse.<Void>builder()
                .code(AppConstants.SUCCESS_CODE)
                .message("Deleted permission")
                .build( );
    }
}
