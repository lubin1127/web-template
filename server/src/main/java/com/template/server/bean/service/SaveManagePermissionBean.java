package com.template.server.bean.service;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author lubin
 * @date 2021/8/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SaveManagePermissionBean {

    private String name;

    private String type;

    private Long parentId;

    private String path;

    private String component;

    private String redirect;

    private String title;

    private String icon;

    private String permission;


}
