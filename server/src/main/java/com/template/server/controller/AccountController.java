package com.template.server.controller;

import com.template.server.service.ManageCacheService;
import com.template.server.service.ManageService;
import com.template.server.vo.RouterVO;
import module.mybatis.plus.entity.ManagePermission;
import module.shiro.realm.bean.RealmBean;
import module.web.controller.BaseController;
import module.web.controller.Result;
import module.web.controller.SuccessResult;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lubin
 * @date 2021/8/12
 */
@RestController
@RequestMapping("/account/")
public class AccountController extends BaseController {

    @Autowired
    private ManageCacheService manageCacheService;

    @Autowired
    private ManageService manageService;

    @PostMapping(value = "permission", produces = MEDIA_TYPE_JSON_UTF_8)
    public String method() {
        Result result = new SuccessResult();
        RealmBean user = (RealmBean) SecurityUtils.getSubject().getPrincipal();
        List<ManagePermission> permissionList = manageCacheService.searchRolePermission(user.getRoleList());
        List<RouterVO> data = manageService.buildRouter(permissionList);
        ((SuccessResult) result).setData(data);
        return this.result(result);
    }

}
