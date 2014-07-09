package com.hadoop.gy404.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import com.hadoop.gy404.intercept.UploadFileIntercept;
import com.hadoop.gy404.model.FileSystem;
import com.hadoop.gy404.service.FileOperateService;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/** 
 *  
 * HDFS file control class(云端文件操作 控制类)
 * 
 */
public class FileOperateController extends Controller {

	private FileOperateService fileoperateservice;
	
	public FileOperateController() throws IOException{
		fileoperateservice=new FileOperateService();
	}
	
	/*upload file to hdfs(上传文件到云端)*/
	@Before(UploadFileIntercept.class)
	public void uploadfile(){
		String dirname=getPara("dirname");
		int dirid=getParaToInt("dirid");
		dirname=URLDecoder.decode(dirname);
		System.out.println(dirname+dirid);
		Controller c=this.keepPara();
		fileoperateservice.uploadfile(c,dirname,dirid);
	}
	
	
    //get folder files list(查询某个目录下的文件列表)
	public void getfilelist(){
		int dirid=getParaToInt("dirid");
		int nowpage=getParaToInt("nowpage");
		Controller c=this.keepPara();
		fileoperateservice.getfilelist(c,dirid,nowpage);
	}
	
	//get files list by keywords(查询含有关键词的文件列表)
	public void getfilelistbykey(){
		String key=getPara("key");
		int nowpage=getParaToInt("nowpage");
		Controller c=this.keepPara();
		fileoperateservice.getfilelistbykey(c,key,nowpage);
	}
	
	//get files list by file type(查询某个类型下的文件列表)
	public void getfilelist1(){
			String filetype=getPara("filetype");
			int nowpage=getParaToInt("nowpage");
			Controller c=this.keepPara();
			fileoperateservice.getfilelist1(c,filetype,nowpage);
		}
		
	//delete hdfs file(删除云端某个文件)
	public void deletefile(){
		int fileid=getParaToInt("fileid");
		Controller c=this.keepPara();
		fileoperateservice.deletefile(c,fileid);
	}
	
	//view hdfs file(查看云端某个文件)
		public void viewfile(){
			int fileid=getParaToInt("fileid");
			Controller c=this.keepPara();
			fileoperateservice.viewfile(c,fileid);
		}
		
	//download hdfs file(下载云端文件)
	public void downloadfile(){
		int fileid=getParaToInt("fileid");
		Controller c=this.keepPara();
		fileoperateservice.downloadfile(c,fileid);
	}
	
	//download windows media player codecs(下载音视频播放插件)
	public void downloadcodecs(){
		String filepath=getPara("filepath");
		System.out.println("filepath:"+filepath);
			Controller c=this.keepPara();
			fileoperateservice.downloadcodecs(c,filepath);
		}
}
