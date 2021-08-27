package com.template.server.service;

import module.mybatis.plus.entity.ManagePermission;

import java.util.LinkedList;
import java.util.List;

/**
 * @author lubin
 * @date 2021/8/11
 */
public interface ManageCacheService {

    List<Long> EMPTY_LIST = new LinkedList<>();

    /**
     * 写入资源 - 自定绑定到root角色
     *
     * @param permission
     */
    void addPermission(ManagePermission permission);

    /**
     * 移除资源
     *
     * @param permission
     */
    void removePermission(ManagePermission permission);

    /**
     * 更新资源
     *
     * @param permission
     */
    void updatePermission(ManagePermission permission);

    /**
     * 写入角色资源
     *
     * @param manageRole
     * @param managePermissionList
     */
    void addRolePermission(Long manageRole, List<Long> managePermissionList);

    /**
     * 更新角色资源
     *
     * @param manageRole
     * @param managePermissionList
     */
    void updateRolePermission(Long manageRole, List<Long> managePermissionList);

    /**
     * 查询角色资源
     *
     * @param roleId
     * @return
     */
    List<ManagePermission> searchRolePermission(List<Long> roleId);

    /**
     * 超级管理员角色ID
     *
     * @return
     */
    Long rootRole();
}
