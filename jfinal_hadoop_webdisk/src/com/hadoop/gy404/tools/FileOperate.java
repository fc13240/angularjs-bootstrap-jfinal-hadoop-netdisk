package com.hadoop.gy404.tools;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileOperate {

	
	
	/**将文件大小--字节表示形式转换为文字说明
	 * 实例代码：
	 *      InputStream in;
		    long s=0;//表示上传文件的大小
			in = new FileInputStream("****");
	        s= in.available();
	 * 
	 * */
	public static String FormetFileSize(long fileS) {//转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }
	
	
	/**将文件类型--字节表示形式转换为文字说明
	 * param:
	 * */
	public static String FormetFileType(String type){
		String s="未知类型";
		String image =  ".swf,.BMP,..png,.GIF,.TIFF,.JPG,.PCX,.TGA,.EXIF,.FPX,.SVG,.PSD,.CDR,.PCD,.DXF,.UFO,.EPS,.AI,.RAW";		
        String doc = ".doc,.docx,.ppt,.pptx,.xls,.xlsx,.txt,.pdf";
        String video =  ".flc,.quicktime,.avi,.f4v,.dvd,.vcd,.rm,.asm,.rmvb,.mpeg,.mov,.asf,.wmv,.navi,.3gp,.video,.mkv,.flv,.mp4";
        String audio =  ".RA,.ACC,.OGG,.MP3,.WMA,.CD,.MIDI,.WAV,.AIFF,.AU,.REALAUDIO,.VQF,.OggVorbis,.AAC,.APE";
        String zip=".zip,.rar,.gzip";//压缩包类型
        
        Map<String,String> typemap=new HashMap<String,String>();
        typemap.put(image, "图片");
        typemap.put(doc, "文档");
        typemap.put(video, "视频");
        typemap.put(audio, "音频");
        typemap.put(zip, "压缩包");
        
        //遍历所有类型与type(文件扩展名)进行匹配
        Iterator i=typemap.entrySet().iterator();
        while(i.hasNext()){//只遍历一次,速度快
        Map.Entry e=(Map.Entry)i.next();
        System.out.println(e.getKey()+"="+e.getValue());
        if(e.getKey().toString().toLowerCase().contains(type.toLowerCase())){
        	s=e.getValue().toString();break;
           }
        }
		return s;
	}
	
	
}
