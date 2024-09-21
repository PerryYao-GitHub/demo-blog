package com.ypy.blogbackend.common;

import lombok.Getter;

import java.util.Objects;

public enum BlogPermission {
    ONLY_ME((byte) 0),
    SYS_USERS((byte) 1),
    EVERYONE((byte) 2);

    @Getter
    private Byte code;

    BlogPermission(Byte code) {
        this.code = code;
    }

    public static boolean isValid(Byte code) {
        for (BlogPermission permission : BlogPermission.values()) {
            if (Objects.equals(permission.code, code)) return true;
        }
        return false;
    }
}
