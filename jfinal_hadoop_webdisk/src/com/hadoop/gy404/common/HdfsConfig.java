package com.hadoop.gy404.common;


import com.hadoop.gy404.controller.DirOperateController;
import com.hadoop.gy404.controller.FileOperateController;
import com.hadoop.gy404.controller.IndexController;
import com.hadoop.gy404.controller.UserOperateController;
import com.hadoop.gy404.intercept.GlobalInterceptor;
import com.hadoop.gy404.model.*;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;

/**
 * jfinal API config(引导式配置)
 */
public class HdfsConfig extends JFinalConfig {
	
	/**
	 * configConstant(配置常量) 
	 */
	public void configConstant(Constants me) {
		loadPropertyFile("mysql_config.txt");				// load property,using getProperty get the value (加载少量必要配置，随后可用getProperty(...)获取值)
		me.setDevMode(getPropertyToBoolean("devMode", false));
		me.setViewType(ViewType.JSP); 							// set viewtype to jsp, the default viewtype is FreeMarker(设置视图类型为Jsp，否则默认为FreeMarker)
		me.setError404View("/404.html");
        me.setError500View("/500.html");
	}
	
	/**
	 * configRoute(配置路由)
	 */
	public void configRoute(Routes me) {
		me.add("/", CommonController.class);
		me.add("/user", UserOperateController.class);
		me.add("/file", FileOperateController.class);
		me.add("/dir", DirOperateController.class);
		me.add("/index", IndexController.class);
	}
	
	/**
	 * configPlugin(配置插件)
	 */
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
		me.add(c3p0Plugin);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		arp.setShowSql(true);
		me.add(arp);
		arp.addMapping("filesystem", FileSystem.class);	// mapping filesystem table to filesystem model(映射filesystem 表到 FileSystem模型)
		arp.addMapping("dir", Dir.class);	// mapping dir table to dir model(映射dir 表到 Dir模型)
		arp.addMapping("userinfo", UserInfo.class);	// mapping userinfo table to userinfo model(映射userinfo 表到 UserInfo模型)
		arp.addMapping("sharetb", ShareTb.class);	// mapping sharetb table to sharetb model(映射sharetb 表到 ShareTb模型)
		arp.addMapping("favoritetb", FavoriteTb.class);	// mapping favoritetb table to favoritetb model(映射favoritetb 表到 FavoriteTb模型)
	}
	
	/**
	 * configInterceptor(配置全局拦截器)
	 */
	public void configInterceptor(Interceptors me) {
		me.add(new SessionInViewInterceptor());
        me.add(new GlobalInterceptor());
	}
	
	/**
	 * configHandler(配置处理器)
	 */
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("base"));
	}
	
	/**
	 * main function(运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此)
	 */
	public static void main(String[] args) {
		JFinal.start("WebRoot", 8087, "/gy404yun", 5);
	}
}
