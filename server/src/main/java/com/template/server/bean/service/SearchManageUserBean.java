package com.template.server.bean.service;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author lubin
 * @date 2021/8/7
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SearchManageUserBean {

    private String userName;

    private String realName;

    private String phone;

    private Integer status;

}
