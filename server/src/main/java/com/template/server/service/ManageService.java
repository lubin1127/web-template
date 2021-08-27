package com.template.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.server.bean.service.SaveManagePermissionBean;
import com.template.server.bean.service.SaveManageRoleBean;
import com.template.server.bean.service.SaveManageUserBean;
import com.template.server.bean.service.SearchManagePermissionBean;
import com.template.server.bean.service.SearchManageRoleBean;
import com.template.server.bean.service.SearchManageUserBean;
import com.template.server.bean.service.UpdateManagePermissionBean;
import com.template.server.bean.service.UpdateManageRoleBean;
import com.template.server.bean.service.UpdateManageUserBean;
import com.template.server.vo.RouterVO;
import module.mybatis.plus.entity.ManagePermission;
import module.mybatis.plus.entity.ManageRole;
import module.mybatis.plus.entity.ManageUser;
import module.web.exception.BizException;

import java.util.List;

/**
 * @author lubin
 * @date 2021/8/7
 */
public interface ManageService {

    /**
     * 添加用户
     *
     * @param bean
     * @return
     * @throws BizException
     */
    ManageUser saveUser(SaveManageUserBean bean) throws BizException;

    /**
     * 更新用户基本信息
     *
     * @param bean
     * @return
     * @throws BizException
     */
    ManageUser updateUser(UpdateManageUserBean bean) throws BizException;

    /**
     * 用户修改密码
     *
     * @param id
     * @param oldPassword
     * @param newPassword
     * @return
     * @throws BizException
     */
    ManageUser updateUserPassword(Long id, String oldPassword, String newPassword) throws BizException;

    /**
     * 重置用户密码
     *
     * @param id
     * @param password
     * @return
     * @throws BizException
     */
    ManageUser resetUserPassword(Long id, String password) throws BizException;


    /**
     * 分页查询用户
     *
     * @param bean
     * @param current
     * @param size
     * @return
     */
    Page<ManageUser> searchUser(SearchManageUserBean bean, int current, int size);

    /**
     * 添加角色
     *
     * @param bean
     * @return
     * @throws BizException
     */
    ManageRole saveRole(SaveManageRoleBean bean) throws BizException;

    /**
     * 更新角色
     *
     * @param bean
     * @return
     * @throws BizException
     */
    ManageRole updateRole(UpdateManageRoleBean bean) throws BizException;


    /**
     * 分页查询角色
     *
     * @param bean
     * @param current
     * @param size
     * @return
     */
    Page<ManageRole> searchRole(SearchManageRoleBean bean, int current, int size);

    /**
     * 角色列表
     *
     * @return
     */
    List<ManageRole> searchRole();

    /**
     * 绑定用户角色关系
     *
     * @param userId
     * @param roleId
     * @throws BizException
     */
    void bindUserAndRole(Long userId, List<Long> roleId) throws BizException;

    /**
     * 查询用户所有角色
     *
     * @param userId
     * @return
     */
    List<Long> searchUseRole(Long userId);

    /**
     * 创建资源
     *
     * @param bean
     * @return
     * @throws BizException
     */
    ManagePermission savePermission(SaveManagePermissionBean bean) throws BizException;

    /**
     * 更新资源
     *
     * @param bean
     * @return
     * @throws BizException
     */
    ManagePermission updatePermission(UpdateManagePermissionBean bean) throws BizException;

    /**
     * 移除资源
     *
     * @param id
     * @throws BizException
     */
    void removePermission(Long id) throws BizException;

    /**
     * 搜索资源
     *
     * @param bean
     * @param current
     * @param size
     * @return
     */
    Page<ManagePermission> searchPermission(SearchManagePermissionBean bean, int current, int size);

    /**
     * 绑定角色和资源
     *
     * @param roleId
     * @param permissionId
     * @throws BizException
     */
    void bindRoleAndPermission(Long roleId, List<Long> permissionId) throws BizException;

    /**
     * 构建路由
     *
     * @param managePermissionList
     * @return
     */
    List<RouterVO> buildRouter(List<ManagePermission> managePermissionList);

}
