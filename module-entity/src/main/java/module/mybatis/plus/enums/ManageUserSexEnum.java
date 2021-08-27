package module.mybatis.plus.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lubin
 * @date 2021/8/7
 */
public enum ManageUserSexEnum {

    /**
     * M = 男
     * F = 女
     */
    M("1"),
    F("2");

    private final String value;

    private static final Map<String, ManageUserSexEnum> m = new HashMap<>();

    static {
        for (ManageUserSexEnum row : ManageUserSexEnum.values()) {
            m.put(row.value, row);
        }
    }

    public static ManageUserSexEnum get(String value) {
        return m.get(value) == null ? M : m.get(value);
    }

    ManageUserSexEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
