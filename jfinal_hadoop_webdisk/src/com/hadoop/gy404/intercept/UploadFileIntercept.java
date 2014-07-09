package com.hadoop.gy404.intercept;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

public class UploadFileIntercept implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {
	Controller c=ai.getController();
	String uploadpath=c.getRequest().getRealPath("/")+"/upload/";
	String str = ".jsp|.asp|.php";//no access extension(不允许的后缀名)
	for(int i=0;i<c.getFiles(uploadpath,1024*1024*1024,"utf-8").size();i++){
		String filename=c.getFiles(uploadpath,1024*1024*1024,"utf-8").get(i).getFileName();
		if(str.contains(filename.substring(filename.lastIndexOf(".")))){
			deletefiles(c);//delete files(删除所有上传文件)
			c.setAttr("message", "error file, don't allow upload！(非法文件，不允许上传！)");
			c.renderJson();
			return;	
		}		
	}
	
    ai.invoke();
    
    deletefiles(c);//delete all upload files(删除所有上传文件)
	}

	/*after upload, delete upload temp files(执行完后删除上传文件)*/
	public void deletefiles(Controller controller){
		String uploadpath=controller.getRequest().getRealPath("/")+"/upload/";
	    for(int i=0;i<controller.getFiles(uploadpath,1024*1024*1024,"utf-8").size();i++){
	    	System.out.println("delete files(删除文件)");
	    	controller.getFiles(uploadpath,1024*1024*1024,"utf-8").get(i).getFile().delete();//delete all upload files(删除所有上传文件)
	    }
	}
	
}
