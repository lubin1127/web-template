package module.shiro.config;

import module.shiro.StatelessDefaultSubjectFactory;
import module.shiro.consts.ShiroConsts;
import module.shiro.filter.DefaultFilter;
import module.shiro.realm.DefaultRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author lubin
 * @date 2021/7/28
 */
@Configuration
public class ShiroConfig {

    @Bean
    public Realm defaultRealm() {
        // Realm实现
        return new DefaultRealm();
    }

    @Bean
    public SubjectFactory subjectFactory() {
        // Subject工厂
        return new StatelessDefaultSubjectFactory();
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        // 会话管理器
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(false);
        return sessionManager;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(Realm defaultRealm, SubjectFactory subjectFactory, DefaultWebSessionManager sessionManager) {
        // 安全管理器
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // realm
        List<Realm> realmList = new LinkedList<>();
        realmList.add(defaultRealm);
        securityManager.setRealms(realmList);
        // subjectDAO
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(Boolean.FALSE.booleanValue());
        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
        defaultSubjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(defaultSubjectDAO);
        // subjectFactory
        securityManager.setSubjectFactory(subjectFactory);
        // sessionManager
        securityManager.setSessionManager(sessionManager);
        // 调用SecurityUtils.setSecurityManager(securityManager)
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        // 安全管理器
        factoryBean.setSecurityManager(securityManager);
        // 定义权限名称
        String token = "token";
        // filters
        Map<String, Filter> filters = new LinkedHashMap();
        filters.put(token, new DefaultFilter());
        factoryBean.setFilters(filters);
        // 定义访问权限
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap();
        filterChainDefinitionMap.put("/login/**", ShiroConsts.ANON);
        filterChainDefinitionMap.put("/favicon.ico", ShiroConsts.ANON);
        filterChainDefinitionMap.put("/**", token);
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return factoryBean;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}
