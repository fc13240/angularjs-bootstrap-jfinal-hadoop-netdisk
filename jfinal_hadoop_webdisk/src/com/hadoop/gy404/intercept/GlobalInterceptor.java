package com.hadoop.gy404.intercept;


import com.hadoop.gy404.model.UserInfo;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StringKit;

public class GlobalInterceptor implements Interceptor {
    @Override
    public void intercept(ActionInvocation ai) {
        Controller controller = ai.getController();

        if(controller.getSessionAttr("userinfo") == null && StringKit.notBlank(controller.getCookie("gy404yunID"))){
            String gy404yunID = controller.getCookie("gy404yunID");
            if(StringKit.notBlank(gy404yunID)){
                String[] NameAndPass = gy404yunID.split("###");
                UserInfo user = null;
                if(NameAndPass != null && NameAndPass.length == 2){
                    user = UserInfo.dao.getByNameAndPassword(NameAndPass[0], NameAndPass[1]);
                }
                if(user != null){
                    controller.getSession().setMaxInactiveInterval(1800);
                    controller.setSessionAttr("user", user);
                    controller.setSessionAttr("userName", user.get("username"));
                }else{
                    ai.getController().removeCookie("gy404yunID");
                }
            }
        }
        ai.invoke();
        controller.setAttr("v", System.currentTimeMillis() + "");
    }
}

