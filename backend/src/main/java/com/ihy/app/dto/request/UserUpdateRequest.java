package com.ihy.app.dto.request;

import com.ihy.app.entity.Users;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    String name;
    String password;
    String phone;
    LocalDate birthday;
}
