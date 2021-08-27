package module.shiro.filter;

import com.alibaba.fastjson.JSON;
import module.shiro.consts.TokenConsts;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author lubin
 * @date 2021/7/28
 */
public abstract class AbstractFilter extends AccessControlFilter {

    protected final Log logger = LogFactory.getLog(this.getClass());

    protected String getToken(HttpServletRequest request) {
        String tokenStr = request.getHeader(TokenConsts.TOKEN);
        if (tokenStr == null) {
            this.logger.error(Thread.currentThread().getStackTrace()[TokenConsts.STACK_TRACE].toString());
            throw new IllegalArgumentException(TokenConsts.NOT_LOGIN);
        }
        return tokenStr;
    }

    protected void writeExceptionMsg(ServletResponse servletResponse, String msg, Integer result) throws IOException {
        Map<String, Object> modelMap = new HashedMap();
        modelMap.put(TokenConsts.RESULT, result);
        modelMap.put(TokenConsts.MSG, msg);
        servletResponse.setContentType(TokenConsts.JSON_UTF_8);
        servletResponse.getWriter().write(JSON.toJSONString(modelMap));
    }

}
