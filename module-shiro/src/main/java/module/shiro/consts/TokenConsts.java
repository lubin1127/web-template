package module.shiro.consts;

/**
 * @author lubin
 * @date 2021/7/28
 */
public interface TokenConsts {

    int STACK_TRACE = 1;

    String TOKEN = "TOKEN";

    Integer TOKEN_ERROR_RESULT = 1001;

    String RESULT = "result";

    String MSG = "msg";

    String JSON_UTF_8 = "application/json; charset=UTF-8";

    String NOT_LOGIN = "尚未登录";

    String CHECK_FAILURE = "身份检查失败";

    String INVALID = "无效身份凭证";

    String OUT_TIME = "身份凭证过期";

    String DECODE_FAILURE = "身份凭证解析失败";


}
