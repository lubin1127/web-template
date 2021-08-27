package module.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * @author lubin
 * @date 2021/8/6
 */
public class BaseController {

    protected static final String MEDIA_TYPE_JSON_UTF_8 = "application/json; charset=UTF-8";

    protected String result(Result res) {
        return JSON.toJSONString(res, SerializerFeature.BrowserCompatible, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty);
    }

    protected static final int ALL_ERRORS_INDEX = 0;

    protected String resultCheckParams(Errors errors) {
        List<ObjectError> allErrors = errors.getAllErrors();
        ObjectError index = allErrors.get(ALL_ERRORS_INDEX);
        return this.result(FailedResult.checkParamsOf(index.getDefaultMessage()));
    }

}
