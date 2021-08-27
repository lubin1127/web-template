package module.shiro.jwt;

/**
 * @author lubin
 * @date 2021/7/27
 */
public enum HeaderEnum {

    DEFAULT(new Header("HS256", "JWT"));

    private final Header header;

    HeaderEnum(Header header) {
        this.header = header;
    }

    public Header getHeader() {
        return header;
    }
}
