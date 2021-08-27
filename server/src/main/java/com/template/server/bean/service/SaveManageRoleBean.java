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
public class SaveManageRoleBean {

    private String name;

    private String description;

    private Long createId;

}
