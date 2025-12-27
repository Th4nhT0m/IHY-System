package com.ihy.app.common.constant;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC)
public class AppConstants {
    public static final String SUCCESS_CODE = "200";

    public static final String[] PUBLIC_ACCESS = {
            "/user/register",
            "/auth/login",
            "/auth/introspect",
            "/auth/logout",
            "/auth/refresh"

    };

}
