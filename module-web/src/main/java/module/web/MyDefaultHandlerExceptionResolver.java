package module.web;

import module.web.consts.ErrorAttributesConsts;
import module.web.consts.WebKeyConsts;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author lubin
 * @create 2020.12.14
 */
public class MyDefaultHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

    private Set<Class<? extends Exception>> customExceptionSet;

    public MyDefaultHandlerExceptionResolver(Set<Class<? extends Exception>> customExceptionSet) {
        this.customExceptionSet = customExceptionSet;
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        try {
            this.logger.error(e.getMessage());
            if (customExceptionSet.contains(e.getClass())) {
                return this.customException(httpServletRequest, httpServletResponse);
            }
            if (e instanceof RuntimeException) {
                return this.runtimeException(httpServletRequest, httpServletResponse);
            }
            if (e instanceof Exception) {
                return this.exception(httpServletRequest, httpServletResponse);
            }
        } catch (Exception ex) {
            this.logger.error(Thread.currentThread().getStackTrace()[WebKeyConsts.STACK_TRACE].toString());
            this.logger.error(ex.getMessage());
        }
        return null;
    }


    protected ModelAndView customException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        httpServletRequest.setAttribute(ErrorAttributesConsts.CUSTOM_ERROR_MESSAGE_KEY, ErrorAttributesConsts.CUSTOM_MESSAGE);
        httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ModelAndView();
    }

    protected ModelAndView runtimeException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        httpServletRequest.setAttribute(ErrorAttributesConsts.CUSTOM_ERROR_MESSAGE_KEY, ErrorAttributesConsts.CUSTOM_RUNTIME_EXCEPTION_MESSAGE);
        httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ModelAndView();
    }

    protected ModelAndView exception(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        httpServletRequest.setAttribute(ErrorAttributesConsts.CUSTOM_ERROR_MESSAGE_KEY, ErrorAttributesConsts.CUSTOM_EXCEPTION_MESSAGE);
        httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ModelAndView();
    }

}
