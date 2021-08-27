package module.shiro.jwt;

import java.io.Serializable;

/**
 * @author lubin
 * @date 2021/7/27
 */
public class Payload implements Serializable {

    /**
     * iss：签发者
     * sub：用户
     * aud：特殊标识 - 角色ID数组
     * exp：过期时间
     * iat：签发时间
     */
    private String iss;

    private String sub;

    private String aud;

    private Long exp;

    private Long iat;

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public Long getIat() {
        return iat;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    public static Payload.Builder newBuilder() {
        return new Payload.Builder();
    }

    public Payload() {
    }

    private Payload(String iss, String sub, String aud, Long exp, Long iat) {
        this.iss = iss;
        this.sub = sub;
        this.aud = aud;
        this.exp = exp;
        this.iat = iat;
    }

    public static class Builder {

        private String iss;

        private String sub;

        private String aud;

        private Long exp;

        private Long iat;

        public Builder iss(String iss) {
            this.iss = iss;
            return this;
        }

        public Builder sub(String sub) {
            this.sub = sub;
            return this;
        }

        public Builder aud(String aud) {
            this.aud = aud;
            return this;
        }

        public Builder exp(Long exp) {
            this.exp = exp;
            return this;
        }

        public Builder iat(Long iat) {
            this.iat = iat;
            return this;
        }

        public Payload build() {
            return new Payload(iss, sub, aud, exp, iat);
        }

    }

}
