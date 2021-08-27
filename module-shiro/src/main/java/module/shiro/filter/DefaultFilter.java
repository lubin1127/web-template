package module.shiro.filter;

import module.shiro.consts.TokenConsts;
import module.shiro.token.DefaultToken;
import org.apache.shiro.authc.UnknownAccountException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lubin
 * @date 2021/7/26
 */
public class DefaultFilter extends AbstractFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        try {
            String tokenStr = this.getToken(request);
            DefaultToken token = new DefaultToken(tokenStr);
            getSubject(servletRequest, servletResponse).login(token);
        } catch (IllegalArgumentException e) {
            this.writeExceptionMsg(servletResponse, e.getMessage(), TokenConsts.TOKEN_ERROR_RESULT);
            return false;
        } catch (UnknownAccountException e) {
            this.writeExceptionMsg(servletResponse, e.getMessage(), TokenConsts.TOKEN_ERROR_RESULT);
            return false;
        } catch (Exception e) {
            this.logger.error(Thread.currentThread().getStackTrace()[TokenConsts.STACK_TRACE].toString());
            this.writeExceptionMsg(servletResponse, TokenConsts.CHECK_FAILURE, TokenConsts.TOKEN_ERROR_RESULT);
            return false;
        }
        return true;
    }


}
