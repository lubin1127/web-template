package module.web;


import module.web.consts.ErrorAttributesConsts;
import module.web.consts.WebKeyConsts;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * @author lubin
 * @create 2020.12.14
 */
public class MyDefaultErrorAttributes extends DefaultErrorAttributes {

    public MyDefaultErrorAttributes() {
        super();
    }

    public MyDefaultErrorAttributes(boolean includeException) {
        super(includeException);
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
        this.copyStatusCode(errorAttributes);
        this.rewriteMessage(errorAttributes, webRequest);
        return errorAttributes;
    }


    private void copyStatusCode(Map<String, Object> errorAttributes) {
        errorAttributes.put(WebKeyConsts.RESULT, errorAttributes.get(WebKeyConsts.STATUS));
    }

    private void rewriteMessage(Map<String, Object> errorAttributes, WebRequest webRequest) {
        Object message = webRequest.getAttribute(ErrorAttributesConsts.CUSTOM_ERROR_MESSAGE_KEY, ErrorAttributesConsts.SCOPE);
        if (message == null) {
            errorAttributes.put(WebKeyConsts.MSG, errorAttributes.get(WebKeyConsts.MESSAGE));
        } else {
            errorAttributes.put(WebKeyConsts.MESSAGE, message);
            errorAttributes.put(WebKeyConsts.MSG, message);
        }
    }

}
