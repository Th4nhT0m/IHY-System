package com.ihy.app.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    String name;
    String password;
    String email;
    String phone;
    LocalDate birthday;
}
