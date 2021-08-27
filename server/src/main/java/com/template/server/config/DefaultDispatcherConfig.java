package com.template.server.config;

import com.template.server.service.ManageCacheService;
import com.template.server.service.impl.ManageCacheServiceImpl;
import module.mybatis.plus.service.IManagePermissionService;
import module.mybatis.plus.service.IManageRolePermissionService;
import module.mybatis.plus.service.IManageRoleService;
import module.mysql.data.source.NormalDataSource;
import module.mysql.data.source.ProdDataSource;
import module.web.MyDefaultErrorAttributes;
import module.web.MyDefaultHandlerExceptionResolver;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lubin
 * @date 2021/6/5
 */
@Configuration
public class DefaultDispatcherConfig {

    @Bean
    public ErrorAttributes errorAttributes() {
        return new MyDefaultErrorAttributes();
    }

    @Bean
    public MyDefaultHandlerExceptionResolver myDefaultHandlerExceptionResolver() {
        Set<Class<? extends Exception>> customExceptionSet = new HashSet<>();
        return new MyDefaultHandlerExceptionResolver(customExceptionSet);
    }

    @Profile({"dev", "test"})
    @Bean
    public DataSource dataSourceDevAndTest() {
        NormalDataSource normalDataSource = new NormalDataSource();
        return normalDataSource.getDataSource();
    }

    @Profile({"prod"})
    @Bean
    public DataSource dataSourceProd() {
        ProdDataSource prodDataSource = new ProdDataSource();
        return prodDataSource.getDataSource();
    }

    @Bean
    public Validator validator() {
        return Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory().getValidator();
    }

    @Bean
    public ManageCacheService manageCacheService(IManagePermissionService iManagePermissionService,
                                                 IManageRoleService iManageRoleService,
                                                 IManageRolePermissionService iManageRolePermissionService) {
        return new ManageCacheServiceImpl(iManagePermissionService, iManageRoleService, iManageRolePermissionService);
    }

}
