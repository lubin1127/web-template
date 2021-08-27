package com.template.server.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author lubin
 * @date 2021/8/12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RouterVO implements Serializable {

    private Long id;

    private Long parentId;

    private String path;

    private String component;

    private String redirect;

    private String name;

    private String permission;

    private Boolean hidden;

    private Meta meta;

    private List<RouterVO> children;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public static class Meta {

        private String title;

        private String icon;
    }

}
