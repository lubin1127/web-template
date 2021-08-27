package module.mybatis.plus.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lubin
 * @date 2021/8/9
 */
public enum ManageRoleStatusEnum {

    /**
     * DEFAULT = 不可修改
     * ENABLE = 启用
     */
    DEFAULT(0),
    ENABLE(1);

    private final Integer value;

    private static final Map<Integer, ManageRoleStatusEnum> m = new HashMap<>();

    static {
        for (ManageRoleStatusEnum row : ManageRoleStatusEnum.values()) {
            m.put(row.value, row);
        }
    }

    public static ManageRoleStatusEnum get(Integer value) {
        return m.get(value) == null ? DEFAULT : m.get(value);
    }

    ManageRoleStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
