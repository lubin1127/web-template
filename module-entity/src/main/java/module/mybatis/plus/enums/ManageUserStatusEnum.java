package module.mybatis.plus.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lubin
 * @date 2021/8/7
 */
public enum ManageUserStatusEnum {

    /**
     * DEFAULT = 不可修改
     * ENABLE = 启用
     * DISABLE = 禁用
     */
    DEFAULT(0),
    ENABLE(1),
    DISABLE(2);

    private final Integer value;

    private static final Map<Integer, ManageUserStatusEnum> m = new HashMap<>();

    static {
        for (ManageUserStatusEnum row : ManageUserStatusEnum.values()) {
            m.put(row.value, row);
        }
    }

    public static ManageUserStatusEnum get(Integer value) {
        return m.get(value) == null ? ENABLE : m.get(value);
    }

    ManageUserStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
