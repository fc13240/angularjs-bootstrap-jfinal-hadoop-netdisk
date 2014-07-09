package com.hadoop.gy404.service;

import java.io.IOException;
import java.sql.Timestamp;

import com.hadoop.gy404.model.Dir;
import com.hadoop.gy404.tools.hadoop.HDFSConfig;
import com.hadoop.gy404.tools.hadoop.HDFSOperation;
import com.jfinal.core.Controller;

public class DirOperateService {

	private HDFSOperation hdfsoperation;//hdfs file oper class
	
	
	public DirOperateService() throws IOException{
		hdfsoperation =new HDFSOperation();
	}
	
	//get all dir info(获取所有文件夹信息)
	public void getAllDir(Controller c) {
		String sql="select * from dir order by createtime";
		c.setAttr("dirlist", Dir.dao.find(sql));
		c.renderJson();
	}


	//add dir(添加文件夹)
	public void addDir(Controller c,String name){
		String isexist="select * from dir where name=?";
		if(Dir.dao.findFirst(isexist, name)!=null)
			c.renderJson("message",name+"-the dir name exist, please change other name.(文件夹已经存在，请更换文件夹名称！)");
		else{
			String dirname=HDFSConfig.getHDFSPath()+name;
			if(hdfsoperation.mkdir(dirname)){
				new Dir().set("name", name).set("hdfspath", dirname)
				.set("createtime", new Timestamp(System.currentTimeMillis()))
				.set("updatetime", new Timestamp(System.currentTimeMillis())).save();
				c.setAttr("message","dir create success!(文件夹创建成功！)");
				c.setAttr("status", 1);
				c.renderJson();
			}
			else{
				c.setAttr("status", 0);
				c.setAttr("message","dir create error!(文件夹创建失败！)");
				c.renderJson();
			}
		 }
	}
	
	
	
	
	
}
