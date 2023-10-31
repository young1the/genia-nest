package com.chunjae.nest.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EncodePasswordUtils {
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
