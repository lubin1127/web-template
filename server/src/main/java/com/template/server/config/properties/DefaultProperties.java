package com.template.server.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lubin
 * @date 2021/8/12
 */
@Configuration
@ConfigurationProperties(prefix = "properties")
public class DefaultProperties {

    private String tokenIss = "temp";

    private long timeOut = 1000L * 60 * 60 * 24;

    public String getTokenIss() {
        return tokenIss;
    }

    public void setTokenIss(String tokenIss) {
        this.tokenIss = tokenIss;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }
}
