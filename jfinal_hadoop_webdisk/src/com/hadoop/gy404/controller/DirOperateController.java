package com.hadoop.gy404.controller;

import java.io.IOException;

import com.hadoop.gy404.service.DirOperateService;
import com.jfinal.core.Controller;

public class DirOperateController extends Controller {

	private DirOperateService diroperateservice;
	
	public DirOperateController() throws IOException{
		diroperateservice = new DirOperateService();
	}
	
	//getAllDir info(获取所有文件夹信息)
	public void getAllDir(){
		Controller c=this.keepPara();
		diroperateservice.getAllDir(c);
	}
	
	//add new dir(创建新文件夹)
	public void addDir(){
		Controller c=this.keepPara();
		String name=getPara("dirname");
		diroperateservice.addDir(c, name);
	}
	
	
}
