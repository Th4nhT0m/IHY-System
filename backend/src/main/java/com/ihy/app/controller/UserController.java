package com.ihy.app.controller;

import com.ihy.app.common.constant.AppConstants;
import com.ihy.app.common.dto.response.ApiResponse;
import com.ihy.app.dto.request.UserCreateRequest;
import com.ihy.app.dto.request.UserUpdateRequest;
import com.ihy.app.dto.response.UserResponse;
import com.ihy.app.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService service;

    @PostMapping("/register")
    public ApiResponse<UserResponse> createUserUser(@RequestBody UserCreateRequest request){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        try {
            apiResponse.setResult(service.createUser(request));
            apiResponse.setCode(AppConstants.SUCCESS_CODE);
        } catch (Exception e) {
            throw e;
        }
        return apiResponse;
    }

    @GetMapping("")
    public ApiResponse<List<UserResponse>> getAllUser(){
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(service.getAllUser());
        apiResponse.setCode(AppConstants.SUCCESS_CODE);
        return apiResponse;
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userId ,@RequestBody UserUpdateRequest request ){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        try {
            service.updateUser(userId,request);
            apiResponse.setCode(AppConstants.SUCCESS_CODE);
        }catch (Exception e){
            throw  e;
        }
        return apiResponse;
    }

    @PutMapping("/delete/{userId}")
    public ApiResponse<UserResponse> deleteUser(@PathVariable String userId){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        try {
            service.disableUser(userId);
            apiResponse.setCode(AppConstants.SUCCESS_CODE);
        }catch (Exception e){
            throw  e;
        }
        return apiResponse;
    }

}
