package com.template.server.controller;

import com.template.server.dto.LoginDTO;
import com.template.server.service.LoginService;
import module.web.controller.BaseController;
import module.web.controller.FailedResult;
import module.web.controller.Result;
import module.web.controller.SuccessResult;
import module.web.exception.BizException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author lubin
 * @date 2021/7/30
 */
@RestController
@RequestMapping("/login/")
public class LoginController extends BaseController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "index", produces = MEDIA_TYPE_JSON_UTF_8)
    public String method(@Valid @RequestBody LoginDTO dto, Errors errors) {
        if (errors.hasErrors()) {
            return this.resultCheckParams(errors);
        }
        Result result = new SuccessResult();
        try {
            String login = loginService.login(dto.getUserName(), dto.getPassword());
            ((SuccessResult) result).setData(login);
        } catch (BizException e) {
            result = FailedResult.defaultException(e.getMessage());
        }
        return this.result(result);
    }

}
