package com.template.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lubin
 * @date 2021/7/26
 */
@SpringBootApplication(scanBasePackages = {"com.template.server", "module.mybatis.plus.service"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
