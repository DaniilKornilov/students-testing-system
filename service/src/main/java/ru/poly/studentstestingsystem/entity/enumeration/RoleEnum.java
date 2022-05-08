package ru.poly.studentstestingsystem.entity.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum RoleEnum {
    ROLE_STUDENT("STUDENT"), ROLE_ADMIN("ADMIN"), ROLE_TEACHER("TEACHER");
    private static final Map<String, RoleEnum> lookup = new HashMap<>();

    static {
        for (RoleEnum roleEnum : RoleEnum.values()) {
            lookup.put(roleEnum.getRoleName(), roleEnum);
        }
    }

    public final String roleName;

    RoleEnum(String roleName) {
        this.roleName = roleName;
    }

    public static RoleEnum get(String roleName) {
        return lookup.get(roleName);
    }

    public String getRoleName() {
        return roleName;
    }
}
