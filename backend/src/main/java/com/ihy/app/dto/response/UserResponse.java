package com.ihy.app.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    String id;
    String name;
    String email;
    String phone;
    LocalDate birthday;
    Set<String> role;
}
