package com.template.server.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lubin
 * @date 2021/8/6
 */
public class LoginDTO implements Serializable {

    @NotNull(message = "用户名不能为空")
    private String userName;
    @NotNull(message = "密码不能为空")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
