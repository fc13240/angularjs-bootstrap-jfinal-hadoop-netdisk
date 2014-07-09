
package com.hadoop.gy404.tools.hadoop;



/** 
 *  
 * HDFS云端路径配置
 * 
 */
public class HDFSConfig {
	
	
	/**
	 * @Title: getHDFSPath
	 * @Description HDFS云端路径配置
	 * @see     HDFS云端路径配置
	 * @return String
	 * @param   对方法中某参数的说明
	 * @example 方法使用例子
	 * */
	public static String  getHDFSPath(){
		return "hdfs://192.168.1.166:9000/input/";//要保证你的hdfs空间中有此路径		
	}
}
