package module.shiro.token;

import module.shiro.consts.TokenConsts;
import module.shiro.jwt.JWT;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author lubin
 * @date 2021/7/28
 */
public class DefaultToken implements AuthenticationToken {

    private final Log logger = LogFactory.getLog(this.getClass());

    private String principal;

    private String credentials;

    private String encodeHeader;

    private String encodePayload;

    private JWT jwt;

    private static final String JWT_SPLIT = "\\.";
    private static final int LENGTH_LIMIT = 3;
    private static final int INDEX_HEADER = 0;
    private static final int INDEX_PAYLOAD = 1;
    private static final int INDEX_SIGNATURE = 2;

    public DefaultToken(String token) {
        String[] split = token.split(JWT_SPLIT);
        if (split.length != LENGTH_LIMIT) {
            this.logger.error(Thread.currentThread().getStackTrace()[TokenConsts.STACK_TRACE].toString());
            throw new IllegalArgumentException(TokenConsts.INVALID);
        }
        this.encodeHeader = split[INDEX_HEADER];
        this.encodePayload = split[INDEX_PAYLOAD];
        this.credentials = split[INDEX_SIGNATURE];
        this.jwt = JWT.newBuilder().headerByEncode(this.encodeHeader).payloadByEncode(this.encodePayload).build();
        jwt.valid(logger);
        this.principal = jwt.getPayload().getSub();
    }

    public String getEncodeHeader() {
        return encodeHeader;
    }

    public String getEncodePayload() {
        return encodePayload;
    }

    public JWT getJwt() {
        return jwt;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }
}
