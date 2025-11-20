package com.ihy.app.service;

import com.ihy.app.common.constant.AppConstants;
import com.ihy.app.common.constant.ErrorCode;
import com.ihy.app.common.exception.AppException;
import com.ihy.app.dto.request.UserCreateRequest;
import com.ihy.app.dto.request.UserUpdateRequest;
import com.ihy.app.dto.response.UserResponse;
import com.ihy.app.entity.Users;
import com.ihy.app.mapper.UsersMapper;
import com.ihy.app.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;

    UsersMapper usersMapper;

    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreateRequest request) {


        //Check if user already exists in database when creating new one
        if(userRepository.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.USER_EXISTS);
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        Users userNew = usersMapper.toCreateUser(request);

        //Set default role
        HashSet<String> roles = new HashSet<>();
        roles.add(AppConstants.ROLE.USER.name());
        userNew.setRole(roles);

        return usersMapper.toUserResponse(userRepository.save(userNew));
    }

    public List<UserResponse> getAllUser() {
        return usersMapper.toResponseUserList(userRepository.findActiveUsers());
    }

    public void updateUser(String userId, UserUpdateRequest request) {
        try {

            Users userExisted = this.getUser(userId);

            // Only encrypt password if changed
            String newPassword = request.getPassword();
            if (newPassword != null && !passwordEncoder.matches(newPassword, userExisted.getPassword())) {
                userExisted.setPassword(passwordEncoder.encode(newPassword));
            }

            usersMapper.toUpdateUser(userExisted, request);
            userRepository.save(userExisted);

        } catch (Exception e) {
            throw e;
        }
    }


    public Users getUser(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException(ErrorCode.USER_NOT_EXISTS.getMessage()));
    }


    public void disableUser(String userId) {
        try {
            Users usersDisable = userRepository.findById(userId).orElse(null);
            if (!usersDisable.equals(null)) {
                usersDisable.setIsActive(0);
                userRepository.save(usersDisable);
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
