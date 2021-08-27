package com.template.server.service.impl;

import com.template.server.service.ManageCacheService;
import module.mybatis.plus.entity.ManagePermission;
import module.mybatis.plus.entity.ManageRole;
import module.mybatis.plus.entity.ManageRolePermission;
import module.mybatis.plus.enums.ManageRoleStatusEnum;
import module.mybatis.plus.service.IManagePermissionService;
import module.mybatis.plus.service.IManageRolePermissionService;
import module.mybatis.plus.service.IManageRoleService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author lubin
 * @date 2021/8/11
 */
public class ManageCacheServiceImpl implements ManageCacheService {

    private final ConcurrentMap<Long, ManagePermission> permissionMap;

    private final ConcurrentMap<Long, List<Long>> roleMap;

    private final Long rootRole;

    public ManageCacheServiceImpl(IManagePermissionService iManagePermissionService,
                                  IManageRoleService iManageRoleService,
                                  IManageRolePermissionService iManageRolePermissionService) {
        // 所有资源
        List<ManagePermission> managePermissionList = iManagePermissionService.list();
        // 资源map
        Map<Long, ManagePermission> managePermissionMap = managePermissionList.stream()
                .collect(Collectors.toMap(ManagePermission::getId, managePermission -> managePermission));
        this.permissionMap = new ConcurrentHashMap<>(managePermissionMap);
        // 所有角色
        Long rootRole = null;
        List<ManageRole> manageRoleList = iManageRoleService.list();
        Map<Long, List<Long>> roleMap = new HashMap<>();
        for (ManageRole row : manageRoleList) {
            roleMap.put(row.getId(), new LinkedList<>());
            ManageRoleStatusEnum manageRoleStatusEnum = ManageRoleStatusEnum.get(row.getStatus());
            if (manageRoleStatusEnum == ManageRoleStatusEnum.DEFAULT) {
                rootRole = row.getId();
            }
        }
        // 所有绑定关系
        List<ManageRolePermission> manageRolePermissions = iManageRolePermissionService.list();
        for (ManageRolePermission row : manageRolePermissions) {
            if (roleMap.containsKey(row.getRoleId()) && managePermissionMap.containsKey(row.getPermissionId())) {
                roleMap.get(row.getRoleId()).add(row.getPermissionId());
            }
        }
        this.roleMap = new ConcurrentHashMap<>(roleMap);
        this.rootRole = rootRole;
    }

    @Override
    public void addPermission(ManagePermission permission) {
        this.permissionMap.put(permission.getId(), permission);
        if (this.rootRole != null) {
            roleMap.get(this.rootRole).add(permission.getId());
        }
    }

    @Override
    public void removePermission(ManagePermission permission) {
        this.permissionMap.remove(permission.getId());
    }

    @Override
    public void updatePermission(ManagePermission permission) {
        this.permissionMap.put(permission.getId(), permission);
    }

    @Override
    public void addRolePermission(Long manageRole, List<Long> managePermissionList) {
        if (roleMap.containsKey(manageRole)) {
            roleMap.get(manageRole).addAll(managePermissionList);
        } else {
            roleMap.put(manageRole, new LinkedList<>(managePermissionList));
        }
    }

    @Override
    public void updateRolePermission(Long manageRole, List<Long> managePermissionList) {
        roleMap.put(manageRole, managePermissionList);
    }

    @Override
    public List<ManagePermission> searchRolePermission(List<Long> roleId) {
        Set<Long> permissionSet = new TreeSet<>();
        for (Long row : roleId) {
            if (roleMap.containsKey(row)) {
                for (Long l : roleMap.get(row)) {
                    permissionSet.add(l);
                }
            }
        }
        List<ManagePermission> res = new LinkedList<>();
        for (Long row : permissionSet) {
            if (permissionMap.containsKey(row)) {
                res.add(permissionMap.get(row));
            }
        }
        return res;
    }

    @Override
    public Long rootRole() {
        return this.rootRole;
    }


}
