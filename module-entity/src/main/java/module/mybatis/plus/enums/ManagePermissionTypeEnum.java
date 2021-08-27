package module.mybatis.plus.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lubin
 * @date 2021/8/11
 */
public enum ManagePermissionTypeEnum {

    MENU("menu", Boolean.TRUE),
    BUTTON("button", Boolean.FALSE);

    private final String value;
    private final Boolean hidden;

    private static final Map<String, ManagePermissionTypeEnum> m = new HashMap<>();

    static {
        for (ManagePermissionTypeEnum row : ManagePermissionTypeEnum.values()) {
            m.put(row.value, row);
        }
    }

    public static ManagePermissionTypeEnum get(String value) {
        return m.get(value) == null ? MENU : m.get(value);
    }

    ManagePermissionTypeEnum(String value, Boolean hidden) {
        this.value = value;
        this.hidden = hidden;
    }

    public String getValue() {
        return value;
    }

    public Boolean getHidden() {
        return hidden;
    }
}
