package module.shiro.realm;

import com.alibaba.fastjson.JSONArray;
import module.shiro.jwt.JWT;
import module.shiro.realm.bean.RealmBean;
import module.shiro.token.DefaultToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.List;

/**
 * @author lubin
 * @date 2021/7/28
 */
public class DefaultRealm extends AuthorizingRealm {

    private final Log logger = LogFactory.getLog(this.getClass());

    private static final String REALM_NAME = "default";

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof DefaultToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 判断是否是REALM_NAME 是的话查询权限 principalCollection.fromRealm(REALM_NAME)
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        DefaultToken token = (DefaultToken) authenticationToken;
        String signature = JWT.signature(token.getEncodeHeader(), token.getEncodePayload());
        Long id = Long.valueOf(token.getPrincipal().toString());
        List<Long> roleList = JSONArray.parseArray(token.getJwt().getPayload().getAud(), Long.class);
        RealmBean realmBean = new RealmBean().setId(id).setRoleList(roleList);
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(realmBean, signature, REALM_NAME);
        return info;
    }
}
