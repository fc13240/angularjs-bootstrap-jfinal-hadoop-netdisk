package com.hadoop.gy404.model;


import java.util.Date;

import com.jfinal.plugin.activerecord.Model;

public class UserInfo extends Model<UserInfo> {
	public static final UserInfo dao = new UserInfo();
	
	public UserInfo getByNameAndPassword(String username, String password){
        return dao.findFirst("select * from userinfo where username=? and userpass=?", username, password);
    }
	
	 public boolean containEmail(String useremail) {
	        return dao.findFirst("select *  from userinfo where useremail=?", useremail) != null;
	    }
	    public boolean containUsername(String username) {
	        return dao.findFirst("select * from userinfo where username=?", username) != null;
	    }

}
