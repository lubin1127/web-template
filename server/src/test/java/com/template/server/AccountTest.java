package com.template.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.server.bean.service.SaveManagePermissionBean;
import com.template.server.bean.service.SaveManageRoleBean;
import com.template.server.bean.service.SaveManageUserBean;
import com.template.server.bean.service.SearchManageUserBean;
import com.template.server.service.ManageCacheService;
import com.template.server.service.ManageService;
import com.template.server.vo.RouterVO;
import module.mybatis.plus.entity.ManagePermission;
import module.mybatis.plus.entity.ManageRole;
import module.mybatis.plus.entity.ManageUser;
import module.mybatis.plus.entity.ManageUserRole;
import module.mybatis.plus.enums.ManagePermissionTypeEnum;
import module.mybatis.plus.enums.ManageRoleStatusEnum;
import module.mybatis.plus.service.IManageRoleService;
import module.mybatis.plus.service.IManageUserRoleService;
import module.mysql.generator.SnowflakeGenerator;
import module.web.exception.BizException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * @author lubin
 * @date 2021/8/7
 */
@SpringBootTest
public class AccountTest {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Test
    public void test(@Autowired ManageService manageService) throws BizException {
        Page<ManageUser> page = manageService.searchUser(new SearchManageUserBean().setPhone("123"), 1, 10);
        System.out.println(page.getTotal());
    }

    @Test
    public void cachePermission(@Autowired ManageCacheService manageCacheService,
                                @Autowired ManageService manageService) {
        List<Long> roleId = new LinkedList<>();
        roleId.add(424981348801970176L);
        List<ManagePermission> managePermissionList = manageCacheService.searchRolePermission(roleId);
        List<RouterVO> buildRouter = manageService.buildRouter(managePermissionList);
        long[] size = new long[buildRouter.size()];
        int i = 0;
        int err = 0;
        for (RouterVO vo : buildRouter) {
            size[i] = vo.getId().longValue();
            if (i > 0) {
                err += size[i] > size[i - 1] ? 1 : 0;
            }
            i++;
        }
        System.out.println(err);
    }


    @Test
    public void saveBatchPermission(@Autowired ManageService manageService) throws BizException {
        List<ManagePermission> firstLevel = new LinkedList<>();
        String firstLevelName = "firstLevel";
        for (int i = 0; i < 10; i++) {
            SaveManagePermissionBean bean = new SaveManagePermissionBean().setName(firstLevelName + i)
                    .setType(ManagePermissionTypeEnum.MENU.getValue()).setParentId(0L);
            ManagePermission permission = manageService.savePermission(bean);
            firstLevel.add(permission);
        }
        Random random = new Random();
        List<ManagePermission> secondLevel = new LinkedList<>();
        String secondLevelName = "secondLevel";
        for (int i = 0; i < 10; i++) {
            SaveManagePermissionBean bean = new SaveManagePermissionBean().setName(secondLevelName + i)
                    .setType(ManagePermissionTypeEnum.MENU.getValue()).setParentId(firstLevel.get(random.nextInt(firstLevel.size())).getId());
            ManagePermission permission = manageService.savePermission(bean);
            secondLevel.add(permission);
        }
        firstLevel.addAll(secondLevel);
        String thirdLevelName = "thirdLevel";
        for (int i = 0; i < 10; i++) {
            ManagePermissionTypeEnum type = random.nextBoolean() ? ManagePermissionTypeEnum.MENU : ManagePermissionTypeEnum.BUTTON;
            SaveManagePermissionBean bean = new SaveManagePermissionBean().setName(thirdLevelName + i)
                    .setType(type.getValue()).setParentId(firstLevel.get(random.nextInt(firstLevel.size())).getId());
            manageService.savePermission(bean);
        }
    }

    @Test
    public void saveManageUserRoot(@Autowired ManageService manageService, @Autowired IManageRoleService iManageRoleService,
                                   @Autowired SnowflakeGenerator snowflakeGenerator, @Autowired IManageUserRoleService iManageUserRoleService) throws BizException {
        // user
        SaveManageUserBean userBean = new SaveManageUserBean().setUserName("root").setPassword("12345678").setRealName("管理员")
                .setStatus(0);
        ManageUser manageUser = manageService.saveUser(userBean);
        // role
        SaveManageRoleBean saveManageRoleBean = new SaveManageRoleBean().setName("root").setDescription("系统所有者");
        ManageRole manageRole = manageService.saveRole(saveManageRoleBean);
        manageRole.setStatus(ManageRoleStatusEnum.DEFAULT.getValue());
        iManageRoleService.updateById(manageRole);
        // user role
        ManageUserRole manageUserRole = new ManageUserRole().setId(snowflakeGenerator.nextId())
                .setUserId(manageUser.getId()).setRoleId(manageRole.getId()).setCreateTime(new Timestamp(System.currentTimeMillis()));
        iManageUserRoleService.save(manageUserRole);
    }

}