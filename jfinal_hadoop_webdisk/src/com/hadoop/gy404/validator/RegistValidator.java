package com.hadoop.gy404.validator;


import com.hadoop.gy404.model.UserInfo;
import com.jfinal.core.Controller;
import com.jfinal.kit.StringKit;
import com.jfinal.validate.Validator;

public class RegistValidator extends Validator {
    @Override
    protected void validate(Controller c) {
        //validateEmail("user.useremail", "msg", "错误的邮箱地址");
       // validateRegex("user.username", "[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,8}", "msg", "用户名的长度介于2-8之间，只能包含中文，数字，字母，下划线");
       // validateRegex("user.userpass", "[a-zA-Z0-9_]{6,12}", "msg", "密码的长度介于6-12之间，只能包含数字，字母，下划线");
       // validateEqualField("user.password", "repassword", "msg", "2次输入的密码不一致");
        String email = c.getPara("useremail");
        System.out.println("email"+email);
        if(StringKit.notBlank(email) && UserInfo.dao.containEmail(email)){
            addError("msg", "该email已经被注册过了：（");
            c.renderJson("msg", "该email已经被注册过了：（");
        }
        String username = c.getPara("username");
        System.out.println("username"+username);
        if(StringKit.notBlank(username) && UserInfo.dao.containUsername(username)){
        	addError("msg", "该用户名已经被注册过了：（");
        	c.renderJson("msg", "该用户名已经被注册过了：（");
        }
    }

    @Override
    protected void handleError(Controller c) {
        //c.keepModel(UserInfo.class);
        //c.renderJson(c.getError());
    }
}
