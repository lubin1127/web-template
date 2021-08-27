package com.template.server.service;

import module.web.exception.BizException;

/**
 * @author lubin
 * @date 2021/8/7
 */
public interface LoginService {

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     * @throws BizException
     */
    String login(String username, String password) throws BizException;

}
