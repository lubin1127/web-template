package module.mybatis.plus.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lubin
 * @date 2021/8/11
 */
public enum ManagePermissionParentEnum {

    ROOT(0L);

    private final Long value;

    private static final Map<Long, ManagePermissionParentEnum> m = new HashMap<>();

    static {
        for (ManagePermissionParentEnum row : ManagePermissionParentEnum.values()) {
            m.put(row.value, row);
        }
    }

    public static ManagePermissionParentEnum get(Long value) {
        return m.get(value) == null ? ROOT : m.get(value);
    }

    public static boolean equalsRoot(Long value) {
        return ROOT.value.longValue() == value.longValue();
    }

    ManagePermissionParentEnum(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
}
