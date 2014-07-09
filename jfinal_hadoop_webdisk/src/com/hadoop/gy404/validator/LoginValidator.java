package com.hadoop.gy404.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class LoginValidator extends Validator{
    @Override
    protected void validate(Controller c) {
    	validateRequired("username", "msg", "用户名不能为空");
        validateRequired("userpass", "msg", "密码不能为空");
    }

    @Override
    protected void handleError(Controller c) {
        c.keepPara("username");
        c.render("/login.jsp");
    }
}