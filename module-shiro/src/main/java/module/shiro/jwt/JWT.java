package module.shiro.jwt;

import com.alibaba.fastjson.JSON;
import module.shiro.consts.TokenConsts;
import org.apache.commons.logging.Log;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author lubin
 * @date 2021/7/27
 */
public class JWT {

    private Header header;

    private Payload payload;

    private static final String TOKEN_FORMAT = "%s.%s.%s";

    public String token() {
        String headerJson = JSON.toJSONString(header);
        String payloadJson = JSON.toJSONString(payload);
        // Base64加密
        String encodeHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));
        String encodePayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
        // 签名
        String signature = JWT.signature(encodeHeader, encodePayload);
        return String.format(TOKEN_FORMAT, encodeHeader, encodePayload, signature);
    }


    private static final int NUMBER_ZERO = 0;
    private static final int NUMBER_ONE = 1;
    private static final int SPLIT_INT = 46;
    private static final String ALGORITHM_NAME = "HmacSHA256";
    private static final String ALGORITHM_NAME_UPPER_CASE = ALGORITHM_NAME.toUpperCase();
    private static final byte[] SECRET_BYTES = "75e82e251b7896be654f4080668b8e06".getBytes(StandardCharsets.UTF_8);

    public static String signature(String encodeHeader, String encodePayload) {
        try {
            // 加密bytes
            byte[] headerBytes = encodeHeader.getBytes(StandardCharsets.UTF_8);
            byte[] encodePayloadBytes = encodePayload.getBytes(StandardCharsets.UTF_8);
            // 签名bytes
            byte[] contentBytes = new byte[headerBytes.length + NUMBER_ONE + encodePayloadBytes.length];
            System.arraycopy(headerBytes, NUMBER_ZERO, contentBytes, NUMBER_ZERO, headerBytes.length);
            contentBytes[headerBytes.length] = SPLIT_INT;
            System.arraycopy(encodePayloadBytes, NUMBER_ZERO, contentBytes, headerBytes.length + NUMBER_ONE, encodePayloadBytes.length);
            // hash运算
            Mac mac = Mac.getInstance(ALGORITHM_NAME);
            mac.init(new SecretKeySpec(SECRET_BYTES, ALGORITHM_NAME_UPPER_CASE));
            byte[] sign = mac.doFinal(contentBytes);
            String signature = Base64.getUrlEncoder().withoutPadding().encodeToString(sign);
            return signature;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void valid(Log logger) {
        // header
        if (!HeaderEnum.DEFAULT.getHeader().equals(this.header)) {
            logger.error(Thread.currentThread().getStackTrace()[TokenConsts.STACK_TRACE].toString());
            throw new IllegalArgumentException(TokenConsts.DECODE_FAILURE);
        }
        // payload
        if (this.payload.getIss() == null || this.payload.getSub() == null || this.payload.getExp() == null || this.payload.getIat() == null) {
            logger.error(Thread.currentThread().getStackTrace()[TokenConsts.STACK_TRACE].toString());
            throw new IllegalArgumentException(TokenConsts.DECODE_FAILURE);
        }
        // 时间判断
        if (System.currentTimeMillis() > this.payload.getExp().longValue()) {
            logger.error(Thread.currentThread().getStackTrace()[TokenConsts.STACK_TRACE].toString());
            throw new IllegalArgumentException(TokenConsts.OUT_TIME);
        }
    }

    public Header getHeader() {
        return header;
    }

    public Payload getPayload() {
        return payload;
    }

    public static JWT.Builder newBuilder() {
        return new JWT.Builder();
    }

    private JWT(Header header, Payload payload) {
        this.header = header;
        this.payload = payload;
    }

    public static class Builder {

        private Header header;

        private Payload payload;

        public Builder header(Header header) {
            this.header = header;
            return this;
        }

        public Builder headerByEncode(String encodeHeader) {
            this.header = JSON.parseObject(new String(Base64.getUrlDecoder().decode(encodeHeader), StandardCharsets.UTF_8), Header.class);
            return this;
        }

        public Builder payload(Payload payload) {
            this.payload = payload;
            return this;
        }

        public Builder payloadByEncode(String encodePayload) {
            this.payload = JSON.parseObject(new String(Base64.getUrlDecoder().decode(encodePayload), StandardCharsets.UTF_8), Payload.class);
            return this;
        }

        public JWT build() {
            return new JWT(header, payload);
        }

    }

}
