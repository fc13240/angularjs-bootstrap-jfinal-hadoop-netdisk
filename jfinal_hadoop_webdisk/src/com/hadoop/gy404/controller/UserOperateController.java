package com.hadoop.gy404.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.hadoop.gy404.validator.*;
import com.hadoop.gy404.model.UserInfo;
import com.hadoop.gy404.tools.MD5Util;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

public class UserOperateController  extends Controller {

    @Before(LoginValidator.class)
    public void login(){
    	Controller c=this.keepPara();
    	String username = getPara("username");
        String userpass = getPara("userpass");
        UserInfo user = UserInfo.dao.getByNameAndPassword(username, MD5Util.MD5String(userpass).toString());
        if (user != null){
            String gy404yunID = username + "###" + MD5Util.MD5String(userpass).toString();
            setCookie("gy404yunID", gy404yunID, 3600*24*30);
            setSessionAttr("user", user);
            setSessionAttr("username", user.get("username"));
            redirect("/index.jsp");
        }else{
            setAttr("msg", "username or password error(用户名或密码错误)");
            render("/login.jsp");
        }
    }

    public void logout(){
        removeSessionAttr("user");
        removeSessionAttr("username");
        removeCookie("gy404yunID");
        redirect("/login.jsp");
    }

    @Before(RegistValidator.class)
    public void save(){
    	String ip = getRequest().getHeader("X-Forwarded-For");
    	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	ip = getRequest().getHeader("Proxy-Client-IP");
    	}
    	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	ip = getRequest().getHeader("WL-Proxy-Client-IP");
    	}
    	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	ip = getRequest().getHeader("HTTP_CLIENT_IP");
    	}
    	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	ip = getRequest().getHeader("HTTP_X_FORWARDED_FOR");
    	}
    	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	ip = getRequest().getRemoteAddr();
    	}
    	UserInfo user = getModel(UserInfo.class);
    	user.set("username", getPara("username"));
    	user.set("userpass", MD5Util.MD5String(getPara("userpass")).toString());
    	user.set("useremail", getPara("useremail"));
    	SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String str = sdf.format(new Date()); 
    	user.set("regdate", str);
    	user.set("userip", ip);
    	user.set("userspace", "1");
        user.save();
        renderJson("msg", "success register , please login!(恭喜你，注册成功，请登录!)");
       // setAttr("msg", "恭喜你，注册成功，请登录：");
       // render("/login.jsp");
    }


}

