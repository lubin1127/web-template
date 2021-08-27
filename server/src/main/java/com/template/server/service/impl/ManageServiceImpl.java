package com.template.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import com.template.server.config.properties.DefaultProperties;
import com.template.server.service.ManageCacheService;
import com.template.server.service.ManageService;
import com.template.server.vo.RouterVO;
import module.mybatis.plus.entity.ManagePermission;
import module.mybatis.plus.entity.ManageRole;
import module.mybatis.plus.entity.ManageRolePermission;
import module.mybatis.plus.entity.ManageUser;
import module.mybatis.plus.entity.ManageUserRole;
import module.mybatis.plus.enums.ManagePermissionParentEnum;
import module.mybatis.plus.enums.ManagePermissionTypeEnum;
import module.mybatis.plus.enums.ManageRoleStatusEnum;
import module.mybatis.plus.enums.ManageUserSexEnum;
import module.mybatis.plus.enums.ManageUserStatusEnum;
import module.mybatis.plus.service.IManagePermissionService;
import module.mybatis.plus.service.IManageRolePermissionService;
import module.mybatis.plus.service.IManageRoleService;
import module.mybatis.plus.service.IManageUserRoleService;
import module.mybatis.plus.service.IManageUserService;
import module.mysql.generator.SnowflakeGenerator;
import module.shiro.jwt.JWT;
import module.web.MyLog;
import module.web.consts.WebKeyConsts;
import module.web.exception.BizException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author lubin
 * @date 2021/8/7
 */
@Service
public class ManageServiceImpl implements ManageService {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private SnowflakeGenerator snowflakeGenerator;

    @Autowired
    private DefaultProperties defaultProperties;

    @Autowired
    private ManageCacheService manageCacheService;

    @Autowired
    private IManageUserService iManageUserService;

    @Autowired
    private IManageRoleService iManageRoleService;

    @Autowired
    private IManageUserRoleService iManageUserRoleService;

    @Autowired
    private IManagePermissionService iManagePermissionService;

    @Autowired
    private IManageRolePermissionService iManageRolePermissionService;

    private static final String USER_USER_NAME = "用户名已存在";
    private static final String SAVE_USER_FAILED = "添加用户失败";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ManageUser saveUser(SaveManageUserBean bean) throws BizException {
        // 用户名查找
        Wrapper<ManageUser> userNameWrapper = Wrappers.<ManageUser>lambdaQuery().eq(ManageUser::getUserName, bean.getUserName());
        List<ManageUser> manageUserList = iManageUserService.list(userNameWrapper);
        if (!manageUserList.isEmpty()) {
            MyLog.log(logger);
            throw new BizException(USER_USER_NAME);
        }
        // id、salt、密码hash
        long id = snowflakeGenerator.nextId();
        long saltLong = snowflakeGenerator.nextId();
        String salt = String.valueOf(Math.abs(String.valueOf(saltLong).hashCode()));
        String password = JWT.signature(bean.getPassword(), salt);
        // 性别、状态
        ManageUserSexEnum sex = ManageUserSexEnum.get(bean.getSex());
        ManageUserStatusEnum status = ManageUserStatusEnum.get(bean.getStatus());
        // 实体
        ManageUser manageUser = new ManageUser().setId(id).setUserName(bean.getUserName()).setPassword(password).setSalt(salt)
                .setRealName(bean.getRealName()).setPhone(bean.getPhone()).setSex(sex.getValue()).setStatus(status.getValue())
                .setCreateId(bean.getCreateId()).setCreateTime(new Timestamp(System.currentTimeMillis()));
        // 添加数据
        if (!iManageUserService.save(manageUser)) {
            MyLog.log(logger);
            throw new BizException(SAVE_USER_FAILED);
        }
        return manageUser;
    }

    private static final String NO_UPDATE = "不可修改";

    private static final String SEARCH_USER_FAILED = "找不到用户";
    private static final String UPDATE_USER_FAILED = "修改用户信息失败";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ManageUser updateUser(UpdateManageUserBean bean) throws BizException {
        ManageUser manageUser = iManageUserService.getById(bean.getId());
        if (manageUser == null) {
            MyLog.log(logger);
            throw new BizException(SEARCH_USER_FAILED);
        }
        // 不可修改
        ManageUserStatusEnum manageUserStatusEnum = ManageUserStatusEnum.get(manageUser.getStatus());
        if (manageUserStatusEnum == ManageUserStatusEnum.DEFAULT) {
            MyLog.log(logger);
            throw new BizException(NO_UPDATE);
        }
        // 更新数据
        ManageUserSexEnum sex = ManageUserSexEnum.get(bean.getSex());
        ManageUserStatusEnum status = ManageUserStatusEnum.get(bean.getStatus());
        Wrapper<ManageUser> updateWrapper = Wrappers.<ManageUser>lambdaUpdate().set(bean.getRealName() != null, ManageUser::getRealName, bean.getRealName())
                .set(bean.getPhone() != null, ManageUser::getPhone, bean.getPhone())
                .set(bean.getSex() != null, ManageUser::getSex, sex.getValue())
                .set(bean.getStatus() != null, ManageUser::getStatus, status.getValue())
                .eq(ManageUser::getId, bean.getId());
        // 更新
        if (!iManageUserService.update(updateWrapper)) {
            MyLog.log(logger);
            throw new BizException(UPDATE_USER_FAILED);
        }
        return iManageUserService.getById(bean.getId());
    }

    private static final String OLD_PASSWORD_FAILED = "原密码输入错误";
    private static final String UPDATE_USER_PASSWORD_FAILED = "修改密码失败";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ManageUser updateUserPassword(Long id, String oldPassword, String newPassword) throws BizException {
        ManageUser manageUser = iManageUserService.getById(id);
        if (manageUser == null) {
            MyLog.log(logger);
            throw new BizException(SEARCH_USER_FAILED);
        }
        // 比对数据
        String check = JWT.signature(oldPassword, manageUser.getSalt());
        if (!check.equals(manageUser.getPassword())) {
            MyLog.log(logger);
            throw new BizException(OLD_PASSWORD_FAILED);
        }
        // 更新数据
        String password = JWT.signature(newPassword, manageUser.getSalt());
        Wrapper<ManageUser> updateWrapper = Wrappers.<ManageUser>lambdaUpdate().set(ManageUser::getPassword, password)
                .eq(ManageUser::getId, id);
        // 更新
        if (!iManageUserService.update(updateWrapper)) {
            MyLog.log(logger);
            throw new BizException(UPDATE_USER_PASSWORD_FAILED);
        }
        return iManageUserService.getById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ManageUser resetUserPassword(Long id, String password) throws BizException {
        ManageUser manageUser = iManageUserService.getById(id);
        if (manageUser == null) {
            MyLog.log(logger);
            throw new BizException(SEARCH_USER_FAILED);
        }
        // 不可修改
        ManageUserStatusEnum manageUserStatusEnum = ManageUserStatusEnum.get(manageUser.getStatus());
        if (manageUserStatusEnum == ManageUserStatusEnum.DEFAULT) {
            MyLog.log(logger);
            throw new BizException(NO_UPDATE);
        }
        // 更新数据
        String pwd = JWT.signature(password, manageUser.getSalt());
        Wrapper<ManageUser> updateWrapper = Wrappers.<ManageUser>lambdaUpdate().set(ManageUser::getPassword, pwd)
                .eq(ManageUser::getId, id);
        // 更新
        if (!iManageUserService.update(updateWrapper)) {
            MyLog.log(logger);
            throw new BizException(UPDATE_USER_PASSWORD_FAILED);
        }
        return iManageUserService.getById(id);
    }

    @Override
    public Page<ManageUser> searchUser(SearchManageUserBean bean, int current, int size) {
        // 查询条件
        ManageUserStatusEnum status = ManageUserStatusEnum.get(bean.getStatus());
        Wrapper<ManageUser> searchWrapper = Wrappers.<ManageUser>lambdaQuery()
                .likeRight(bean.getPhone() != null, ManageUser::getPhone, bean.getPhone())
                .likeRight(bean.getUserName() != null, ManageUser::getUserName, bean.getUserName())
                .likeRight(bean.getRealName() != null, ManageUser::getRealName, bean.getRealName())
                .eq(bean.getStatus() != null, ManageUser::getStatus, status.getValue())
                .orderByDesc(ManageUser::getId);
        // 分页查询
        Page<ManageUser> iPage = new Page<>(current, size);
        return iManageUserService.page(iPage, searchWrapper);
    }


    private static final String SAVE_ROLE_FAILED = "添加角色失败";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ManageRole saveRole(SaveManageRoleBean bean) throws BizException {
        long id = snowflakeGenerator.nextId();
        ManageRole role = new ManageRole().setId(id).setName(bean.getName()).setDescription(bean.getDescription())
                .setStatus(ManageRoleStatusEnum.ENABLE.getValue()).setCreateId(bean.getCreateId())
                .setCreateTime(new Timestamp(System.currentTimeMillis()));
        // 更新操作
        if (!iManageRoleService.save(role)) {
            MyLog.log(logger);
            throw new BizException(SAVE_ROLE_FAILED);
        }
        // 角色写入缓存
        manageCacheService.addRolePermission(role.getId(), ManageCacheService.EMPTY_LIST);
        return role;
    }

    private static final String SEARCH_ROLE_FAILED = "找不到角色";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ManageRole updateRole(UpdateManageRoleBean bean) throws BizException {
        ManageRole role = iManageRoleService.getById(bean.getId());
        if (role == null) {
            MyLog.log(logger);
            throw new BizException(SEARCH_ROLE_FAILED);
        }
        // 不可修改
        ManageRoleStatusEnum manageRoleStatusEnum = ManageRoleStatusEnum.get(role.getStatus());
        if (manageRoleStatusEnum == ManageRoleStatusEnum.DEFAULT) {
            MyLog.log(logger);
            throw new BizException(NO_UPDATE);
        }
        // 更新数据
        Wrapper<ManageRole> updateWrapper = Wrappers.<ManageRole>lambdaUpdate()
                .set(bean.getName() != null, ManageRole::getName, bean.getName())
                .set(bean.getDescription() != null, ManageRole::getDescription, bean.getDescription())
                .eq(ManageRole::getId, bean.getId());
        if (!iManageRoleService.update(updateWrapper)) {
            MyLog.log(logger);
            throw new BizException();
        }
        return iManageRoleService.getById(bean.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<ManageRole> searchRole(SearchManageRoleBean bean, int current, int size) {
        // 分页查询
        Page<ManageRole> iPage = new Page<>(current, size);
        return iManageRoleService.page(iPage, Wrappers.emptyWrapper());
    }

    @Override
    public List<ManageRole> searchRole() {
        Wrapper<ManageRole> wrapper = Wrappers.<ManageRole>lambdaQuery()
                .eq(ManageRole::getStatus, ManageRoleStatusEnum.ENABLE.getValue());
        return iManageRoleService.list(wrapper);
    }

    private static final String REMOVE_USER_ROLE_BIND_FAILED = "移除用户角色绑定失败失败";
    private static final String USER_ROLE_BIND_FAILED = "用户绑定角色失败";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bindUserAndRole(Long userId, List<Long> roleId) throws BizException {
        ManageUser manageUser = iManageUserService.getById(userId);
        if (manageUser == null) {
            MyLog.log(logger);
            throw new BizException(SEARCH_USER_FAILED);
        }
        // 不可修改
        ManageUserStatusEnum manageUserStatusEnum = ManageUserStatusEnum.get(manageUser.getStatus());
        if (manageUserStatusEnum == ManageUserStatusEnum.DEFAULT) {
            MyLog.log(logger);
            throw new BizException(NO_UPDATE);
        }
        // 删除旧的绑定关系
        Wrapper<ManageUserRole> deleteWrapper = Wrappers.<ManageUserRole>lambdaQuery()
                .eq(ManageUserRole::getUserId, userId);
        if (iManageUserRoleService.count(deleteWrapper) > WebKeyConsts.AFFECTED_ROWS_ZERO && !iManageUserRoleService.remove(deleteWrapper)) {
            MyLog.log(logger);
            throw new BizException(REMOVE_USER_ROLE_BIND_FAILED);
        }
        if (!roleId.isEmpty()) {
            // 角色校验
            List<ManageRole> manageRoleList = iManageRoleService.listByIds(roleId);
            if (manageRoleList.size() != roleId.size()) {
                MyLog.log(logger);
                throw new BizException(SEARCH_ROLE_FAILED);
            }
            // 构建新的绑定关系
            List<ManageUserRole> batchList = new ArrayList<>(roleId.size());
            // 循环构建实体
            for (Long r : roleId) {
                long id = snowflakeGenerator.nextId();
                batchList.add(new ManageUserRole().setId(id).setUserId(userId).setRoleId(r).setCreateTime(new Timestamp(System.currentTimeMillis())));
            }
            // 批量创建
            if (!iManageUserRoleService.saveBatch(batchList, batchList.size())) {
                MyLog.log(logger);
                throw new BizException(USER_ROLE_BIND_FAILED);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Long> searchUseRole(Long userId) {
        Wrapper<ManageUserRole> wrapper = Wrappers.<ManageUserRole>lambdaQuery()
                .eq(ManageUserRole::getUserId, userId);
        List<ManageUserRole> manageUserRoleList = iManageUserRoleService.list(wrapper);
        return manageUserRoleList.stream().map(ManageUserRole::getRoleId).collect(Collectors.toList());
    }

    private static final String PARENT_MENU_NOT_FOUND = "父级资源找不到";
    private static final String SAVE_PERMISSION_FAILED = "添加资源失败";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ManagePermission savePermission(SaveManagePermissionBean bean) throws BizException {
        // 父级ID验证
        Long parentId = ManagePermissionParentEnum.ROOT.getValue();
        if (bean.getParentId() != null && parentId.longValue() != bean.getParentId().longValue()) {
            ManagePermission parentPermission = iManagePermissionService.getById(bean.getParentId());
            if (parentPermission == null) {
                MyLog.log(logger);
                throw new BizException(PARENT_MENU_NOT_FOUND);
            }
            parentId = bean.getParentId();
        }
        // 添加权限
        long id = snowflakeGenerator.nextId();
        ManagePermissionTypeEnum typeEnum = ManagePermissionTypeEnum.get(bean.getType());
        ManagePermission managePermission = new ManagePermission().setId(id).setName(bean.getName()).setType(typeEnum.getValue())
                .setParentId(parentId).setPath(bean.getPath()).setComponent(bean.getComponent()).setRedirect(bean.getRedirect())
                .setTitle(bean.getTitle()).setIcon(bean.getIcon()).setPermission(bean.getPermission())
                .setCreateTime(new Timestamp(System.currentTimeMillis()));
        if (!iManagePermissionService.save(managePermission)) {
            MyLog.log(logger);
            throw new BizException(SAVE_PERMISSION_FAILED);
        }
        // 超级管理员绑定资源
        Long rootRole = manageCacheService.rootRole();
        long rolePermissionId = snowflakeGenerator.nextId();
        ManageRolePermission manageRolePermission = new ManageRolePermission().setId(rolePermissionId).setRoleId(rootRole)
                .setPermissionId(managePermission.getId()).setCreateTime(new Timestamp(System.currentTimeMillis()));
        if (!iManageRolePermissionService.save(manageRolePermission)) {
            MyLog.log(logger);
            throw new BizException(ROLE_PERMISSION_BIND_FAILED);
        }
        // 资源写入缓存
        manageCacheService.addPermission(managePermission);
        return managePermission;
    }

    private static final String SEARCH_PERMISSION_FAILED = "资源找不到";
    private static final String UPDATE_PERMISSION_FAILED = "更新资源失败";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ManagePermission updatePermission(UpdateManagePermissionBean bean) throws BizException {
        ManagePermission permission = iManagePermissionService.getById(bean.getId());
        if (permission == null) {
            MyLog.log(logger);
            throw new BizException(SEARCH_PERMISSION_FAILED);
        }
        // 更新
        Wrapper<ManagePermission> updateWrapper = Wrappers.<ManagePermission>lambdaUpdate()
                .set(bean.getName() != null, ManagePermission::getName, bean.getName())
                .set(bean.getPath() != null, ManagePermission::getPath, bean.getPath())
                .set(bean.getComponent() != null, ManagePermission::getComponent, bean.getComponent())
                .set(bean.getRedirect() != null, ManagePermission::getRedirect, bean.getRedirect())
                .set(bean.getTitle() != null, ManagePermission::getTitle, bean.getTitle())
                .set(bean.getIcon() != null, ManagePermission::getIcon, bean.getIcon())
                .set(bean.getPermission() != null, ManagePermission::getPermission, bean.getPermission())
                .eq(ManagePermission::getId, bean.getId());
        if (!iManagePermissionService.update(updateWrapper)) {
            MyLog.log(logger);
            throw new BizException(UPDATE_PERMISSION_FAILED);
        }
        ManagePermission afterUpdate = iManagePermissionService.getById(bean.getId());
        // 缓存更新资源
        manageCacheService.updatePermission(afterUpdate);
        return afterUpdate;
    }

    private static final String HAS_ROLE_BIND_PERMISSION = "存在角色绑定该资源";
    private static final String REMOVE_PERMISSION_FAILED = "延迟资源失败";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removePermission(Long id) throws BizException {
        Wrapper<ManageRolePermission> wrapper = Wrappers.<ManageRolePermission>lambdaQuery()
                .eq(ManageRolePermission::getPermissionId, id);
        List<ManageRolePermission> rolePermissionList = iManageRolePermissionService.list(wrapper);
        if (!rolePermissionList.isEmpty()) {
            MyLog.log(logger);
            throw new BizException(HAS_ROLE_BIND_PERMISSION);
        }
        ManagePermission permission = iManagePermissionService.getById(id);
        if (permission == null) {
            MyLog.log(logger);
            throw new BizException(SEARCH_PERMISSION_FAILED);
        }
        if (!iManagePermissionService.removeById(id)) {
            MyLog.log(logger);
            throw new BizException(REMOVE_PERMISSION_FAILED);
        }
        // 缓存移除资源
        manageCacheService.removePermission(permission);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<ManagePermission> searchPermission(SearchManagePermissionBean bean, int current, int size) {
        ManagePermissionTypeEnum typeEnum = ManagePermissionTypeEnum.get(bean.getType());
        Wrapper<ManagePermission> wrapper = Wrappers.<ManagePermission>lambdaQuery()
                .likeRight(bean.getName() != null, ManagePermission::getName, bean.getName())
                .likeRight(bean.getTitle() != null, ManagePermission::getTitle, bean.getTitle())
                .likeRight(bean.getPermission() != null, ManagePermission::getPermission, bean.getPermission())
                .eq(bean.getType() != null, ManagePermission::getType, typeEnum.getValue());
        // 分页查询
        Page<ManagePermission> iPage = new Page<>(current, size);
        return iManagePermissionService.page(iPage, wrapper);
    }

    private static final String REMOVE_ROLE_PERMISSION_BIND_FAILED = "移除角色资源绑定失败";
    private static final String ROLE_PERMISSION_BIND_FAILED = "角色绑定资源失败";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bindRoleAndPermission(Long roleId, List<Long> permissionId) throws BizException {
        ManageRole manageRole = iManageRoleService.getById(roleId);
        if (manageRole == null) {
            MyLog.log(logger);
            throw new BizException(SEARCH_ROLE_FAILED);
        }
        // 不可修改
        ManageRoleStatusEnum manageRoleStatusEnum = ManageRoleStatusEnum.get(manageRole.getStatus());
        if (manageRoleStatusEnum == ManageRoleStatusEnum.DEFAULT) {
            MyLog.log(logger);
            throw new BizException(NO_UPDATE);
        }
        // 删除之前绑定关系
        Wrapper<ManageRolePermission> deleteWrapper = Wrappers.<ManageRolePermission>lambdaQuery()
                .eq(ManageRolePermission::getRoleId, roleId);
        if (iManageRolePermissionService.count(deleteWrapper) > WebKeyConsts.AFFECTED_ROWS_ZERO && !iManageRolePermissionService.remove(deleteWrapper)) {
            MyLog.log(logger);
            throw new BizException(REMOVE_ROLE_PERMISSION_BIND_FAILED);
        }
        if (!permissionId.isEmpty()) {
            // 资源校验
            List<ManagePermission> managePermissionList = iManagePermissionService.listByIds(permissionId);
            if (managePermissionList.size() != permissionId.size()) {
                MyLog.log(logger);
                throw new BizException(SEARCH_PERMISSION_FAILED);
            }
            // 父级链式校验
            Set<Long> managePermissionSet = new HashSet<>(managePermissionList.stream().map(ManagePermission::getId).collect(Collectors.toList()));
            this.chainPermission(managePermissionList, managePermissionSet);
            // 构建新的绑定关系
            List<ManageRolePermission> batchList = new ArrayList<>(permissionId.size());
            // 循环构建实体
            for (Long p : managePermissionSet) {
                long id = snowflakeGenerator.nextId();
                batchList.add(new ManageRolePermission().setId(id).setRoleId(roleId).setPermissionId(p).setCreateTime(new Timestamp(System.currentTimeMillis())));
            }
            // 批量创建
            if (!iManageRolePermissionService.saveBatch(batchList, batchList.size())) {
                MyLog.log(logger);
                throw new BizException(ROLE_PERMISSION_BIND_FAILED);
            }
            // 缓存更新角色与资源关系
            manageCacheService.updateRolePermission(roleId, managePermissionSet.stream().collect(Collectors.toList()));
        }
    }

    private void chainPermission(List<ManagePermission> managePermissionList, Set<Long> set) {
        List<Long> addPermissionIdList = new LinkedList<>();
        for (ManagePermission row : managePermissionList) {
            if (!set.contains(row.getParentId())) {
                addPermissionIdList.add(row.getParentId());
            }
        }
        // 父级扩展
        if (!addPermissionIdList.isEmpty()) {
            List<ManagePermission> addList = iManagePermissionService.listByIds(addPermissionIdList);
            set.addAll(addList.stream().map(ManagePermission::getId).collect(Collectors.toList()));
            this.chainPermission(addList, set);
        }
    }

    @Override
    public List<RouterVO> buildRouter(List<ManagePermission> managePermissionList) {
        Map<Long, RouterVO> map = new TreeMap<>();
        long rootValue = ManagePermissionParentEnum.ROOT.getValue().longValue();
        for (ManagePermission row : managePermissionList) {
            RouterVO vo = this.transformRouterVO(row);
            if (map.containsKey(row.getParentId())) {
                map.get(row.getParentId()).getChildren().add(vo);
            }
            map.put(vo.getId(), vo);
        }
        return map.values().stream().filter(vo -> rootValue == vo.getParentId().longValue()).collect(Collectors.toList());
    }

    private RouterVO transformRouterVO(ManagePermission managePermission) {
        RouterVO.Meta meta = new RouterVO.Meta().setTitle(managePermission.getTitle()).setIcon(managePermission.getIcon());
        ManagePermissionTypeEnum typeEnum = ManagePermissionTypeEnum.get(managePermission.getType());
        RouterVO vo = new RouterVO().setId(managePermission.getId()).setParentId(managePermission.getParentId())
                .setPath(managePermission.getPath()).setComponent(managePermission.getComponent()).setRedirect(managePermission.getRedirect())
                .setName(managePermission.getName()).setPermission(managePermission.getPermission()).setHidden(typeEnum.getHidden())
                .setMeta(meta).setChildren(new LinkedList<>());
        return vo;
    }

}
