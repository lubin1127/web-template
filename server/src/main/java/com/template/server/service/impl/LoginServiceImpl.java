package com.template.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.template.server.config.properties.DefaultProperties;
import com.template.server.service.LoginService;
import module.mybatis.plus.entity.ManageUser;
import module.mybatis.plus.entity.ManageUserRole;
import module.mybatis.plus.service.IManageUserRoleService;
import module.mybatis.plus.service.IManageUserService;
import module.shiro.jwt.Header;
import module.shiro.jwt.HeaderEnum;
import module.shiro.jwt.JWT;
import module.shiro.jwt.Payload;
import module.web.MyLog;
import module.web.exception.BizException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lubin
 * @date 2021/8/7
 */
@Service
public class LoginServiceImpl implements LoginService {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private DefaultProperties defaultProperties;

    @Autowired
    private IManageUserService iManageUserService;

    @Autowired
    private IManageUserRoleService iManageUserRoleService;

    private static final String LOGIN_FAILED = "用户名或者密码错误";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String login(String username, String password) throws BizException {
        // 根据用户名获取用户
        Wrapper<ManageUser> userWrapper = Wrappers.<ManageUser>lambdaQuery()
                .eq(ManageUser::getUserName, username);
        ManageUser manageUser = iManageUserService.getOne(userWrapper);
        if (manageUser == null) {
            MyLog.log(logger);
            throw new BizException(LOGIN_FAILED);
        }
        // 比对密码
        String pwd = JWT.signature(password, manageUser.getSalt());
        if (!manageUser.getPassword().equals(pwd)) {
            MyLog.log(logger);
            throw new BizException(LOGIN_FAILED);
        }
        // 用户角色
        Wrapper<ManageUserRole> userRoleWrapper = Wrappers.<ManageUserRole>lambdaQuery()
                .eq(ManageUserRole::getUserId, manageUser.getId());
        List<ManageUserRole> userRoleList = iManageUserRoleService.list(userRoleWrapper);
        List<Long> roleIdList = userRoleList.stream().map(ManageUserRole::getRoleId).collect(Collectors.toList());
        // 生成token
        long iat = System.currentTimeMillis();
        long exp = iat + defaultProperties.getTimeOut();
        Header header = HeaderEnum.DEFAULT.getHeader();
        Payload payload = Payload.newBuilder().iss(defaultProperties.getTokenIss()).sub(manageUser.getId().toString())
                .aud(JSON.toJSONString(roleIdList)).exp(exp).iat(iat).build();
        JWT jwt = JWT.newBuilder().header(header).payload(payload).build();
        return jwt.token();
    }
}
