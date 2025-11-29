package com.ihy.app.dto.response;

import com.ihy.app.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    String id;
    String name;
    String email;
    String phone;
    LocalDate birthday;
    Set<RoleResponse> roles;
}
