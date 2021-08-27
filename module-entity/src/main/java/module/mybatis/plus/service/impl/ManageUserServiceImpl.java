package module.mybatis.plus.service.impl;

import module.mybatis.plus.entity.ManageUser;
import module.mybatis.plus.mapper.ManageUserMapper;
import module.mybatis.plus.service.IManageUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lubin
 * @since 2021-08-07
 */
@Service
public class ManageUserServiceImpl extends ServiceImpl<ManageUserMapper, ManageUser> implements IManageUserService {

}
