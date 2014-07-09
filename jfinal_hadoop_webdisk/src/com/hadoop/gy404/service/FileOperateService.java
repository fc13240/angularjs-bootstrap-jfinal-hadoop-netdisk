package com.hadoop.gy404.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import com.hadoop.gy404.model.Dir;
import com.hadoop.gy404.model.FileSystem;
import com.hadoop.gy404.model.FileSystem1;
import com.hadoop.gy404.tools.Analyze;
import com.hadoop.gy404.tools.DocConverter;
import com.hadoop.gy404.tools.FileOperate;
import com.hadoop.gy404.tools.FileUtil;
import com.hadoop.gy404.tools.IndexUtils;
import com.hadoop.gy404.tools.UUID;
import com.hadoop.gy404.tools.hadoop.HDFSConfig;
import com.hadoop.gy404.tools.hadoop.HDFSOperation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

public class FileOperateService {

	private HDFSOperation hdfsoperation;// hdfs file oper class(文件操作工具类)

	public FileOperateService() throws IOException {
		hdfsoperation = new HDFSOperation();
	}

	// upload file to hdfs(上传文件到云盘)
	public void uploadfile(Controller c, String dirname, int dirid) {
		UploadFile file = null;
		file = c.getFile();
		String prefix = file.getFileName().substring(
				file.getFileName().lastIndexOf(".") + 1);// get file
															// extension(获取文件扩展名)
		String filetext = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		File filetemp = new File(file.getSaveDirectory() + file.getFileName());
		if (prefix.equalsIgnoreCase(".txt")) {
			Date date1 = new Date();
			filetext = this.txtRead(filetemp);
			Date date2 = new Date();
			long time = date2.getTime() - date1.getTime();
			System.out.println("txt extract time:(txt抽取时间：)" + (float) time / 1000 + "秒");
			String str = sdf.format(new Date());
		} else {
			Date date1 = new Date();
			// Extract file text (抽取文件内容)
			Analyze an = new Analyze();
			an.textExtract(filetemp);
			filetext = an.getText();
			Date date2 = new Date();
			long time = date2.getTime() - date1.getTime();
			System.out.println("file text Extract time:(抽词时间：)" + (float) time / 1000 + "秒");
			String str = sdf.format(new Date());
		}
		String filenamestr = UUID.randomUUID().toString() + "." + prefix;

		String hdfspath = HDFSConfig.getHDFSPath() + dirname + "/"
				+ filenamestr;
		System.out.println("hdfs file path:(上传文件的云端路径为：)" + hdfspath);
		if (FileSystem.dao.findFirst(
				"select * from filesystem where filepath=?", hdfspath) != null) {
			c.setAttr("message", "file exist!(文件已经存在，请重新上传新文件！)");
			c.renderJson();
		}

		InputStream in;
		long s = 0;// file size(表示上传文件的大小)
		try {
			in = new FileInputStream(filetemp);
			s = in.available();
			if (hdfsoperation.upLoad(in, hdfspath)) {

				// insert mysql db(插入数据库)
				new FileSystem()
						.set("filename", file.getFileName())
						.set("filepath", hdfspath)
						.set("filesize", FileOperate.FormetFileSize(s))
						.set("filetype", FileOperate.FormetFileType(prefix))
						.set("fileextension", prefix)
						.set("dir", dirid)
						.set("uploadtime",
								new Timestamp(System.currentTimeMillis()))
						.set("mendtime",
								new Timestamp(System.currentTimeMillis()))
						.set("filetext", filetext).save();

				Dir dir = Dir.dao.findById(dirid);// update parent dir file num(更新父目录包含文件数量)
				String sonfilenum_new = String.valueOf(Integer.parseInt(dir
						.getStr("sonfilenum")) + 1);
				dir.set("sonfilenum", sonfilenum_new).update();

				String localPath = c.getRequest().getRealPath("/")
						+ "/lucenceindex/";
				String indexFile = localPath + "poiIdext";
				String storeIdFile = localPath + "storeId.txt";
				if (!new File(indexFile).exists())
					new File(indexFile).mkdirs();
				if (!new File(storeIdFile).exists())
					new File(storeIdFile).createNewFile();

				IndexUtils.buildIndex(indexFile, storeIdFile);

				c.setAttr("message", "file upload success!(文件上传成功了！)");
				c.renderJson();
			} else {
				c.setAttr("message", "file upload error!(文件上传失败！)");
				c.renderJson();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// get dir file list(查询某个目录下的文件列表)
	public void getfilelist(Controller c, int dirid, int nowpage) {
		String select = "select *";
		String condition = "from filesystem where dir='" + dirid + "'";
		Page<FileSystem> filelist = FileSystem.dao.paginate(nowpage, 5, select,
				condition);
		c.setAttr("filelist", filelist);
		c.setAttr("filecount", filelist.getTotalRow());
		System.out.println(filelist.getTotalRow());
		c.renderJson();
	}

	public void getfilelistbykey(Controller c, String key, int nowpage) {
		/*
		 * String localPath=c.getRequest().getRealPath("/")+"/lucenceindex/";
		 * String indexFile = localPath+"poiIdext"; Directory dir = null; try {
		 * dir =FSDirectory.open(new File(indexFile)); } catch (IOException e1)
		 * { // TODO Auto-generated catch block e1.printStackTrace(); }
		 * IndexReader reader = null; try { reader = DirectoryReader.open(dir);
		 * } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } IndexSearcher search = new
		 * IndexSearcher(reader); Date date1 = new Date(); List<FileSystem1>
		 * filelist = IndexUtils.queryByOneKey(search, "filetext", key); Date
		 * date2 = new Date(); System.out.println("耗时：" + (date2.getTime() -
		 * date1.getTime()) + "ms\n" + filelist.size()+
		 * "条=======================================单字段查询"); SELECT * FROM
		 * Persons WHERE City LIKE 'N%'
		 */
		String select = "select *";
		String condition = "from filesystem where filetext like '%" + key
				+ "%'";
		Page<FileSystem> filelist = FileSystem.dao.paginate(nowpage, 5, select,
				condition);
		c.setAttr("filelist", filelist);
		c.setAttr("filecount", filelist.getTotalRow());
		System.out.println(filelist.getTotalRow());
		c.renderJson();
	}

	// get file list by file type(查询某个目录下的文件列表)
	public void getfilelist1(Controller c, String filetype, int nowpage) {
		String select = "select *";
		String condition = "from filesystem where filetype='" + filetype + "'";
		Page<FileSystem> filelist = FileSystem.dao.paginate(nowpage, 5, select,
				condition);
		c.setAttr("filelist", filelist);
		c.setAttr("filecount", filelist.getTotalRow());
		System.out.println(filelist.getTotalRow());
		c.renderJson();
	}

	// delete hdfs file(删除云端某个文件)
	public void deletefile(Controller c, int fileid) {
		FileSystem filesystem = FileSystem.dao.findFirst(
				"select * from filesystem where id=?", fileid);
		if (filesystem == null)
			c.renderJson("message", "file not exist!(文件不存在！)");
		else {
			if (hdfsoperation.deletePath(filesystem.getStr("filepath"))) {

				Dir dir = Dir.dao.findById(filesystem.getInt("dir"));// 更新数据库中当前目录下的文件数量
				dir.set("sonfilenum",
						String.valueOf(Integer.parseInt(dir
								.getStr("sonfilenum")) - 1)).update();

				FileSystem.dao.deleteById(filesystem.getInt("id"));// 删除数据库记录
				c.setAttr("message", "file delete success!(文件删除成功！)");
				c.renderJson("message", "file delete success!(文件删除成功！)");
			} else
				c.renderJson("message", "file delete error!(文件删除错误！)");
		}
	}

	// view hdfs file(查看云端某个文件)
	public void viewfile(Controller c, int fileid) {
		FileSystem filesystem = FileSystem.dao.findFirst(
				"select * from filesystem where id=?", fileid);
		if (filesystem == null)
			c.renderJson("message", "file not exist!(文件不存在！)");
		else {
			String extension = filesystem.getStr("fileextension");
			String filename = filesystem.getStr("filename");
			String filenamestr = UUID.randomUUID().toString();
			;
			String filepaths = "/download/" + filenamestr + "." + extension;
			String localPath = c.getRequest().getRealPath("/") + "/download/"
					+ filenamestr + "." + extension;// 设置下载文件存放的临时目录
			System.out.println("temp path:(临时保存路径：)" + localPath);
			if (hdfsoperation.downFromCloud(filesystem.getStr("filepath"),
					localPath)) {// download file success(表示下载文件成功)
				File filetemp = new File(localPath);
				String newfilepath1 = c.getRequest().getRealPath("/")
						+ "/appfile\\" + filenamestr + "\\";
				if (!new File(newfilepath1).exists())
					new File(newfilepath1).mkdirs();
				String newfilepath2 = newfilepath1 + filenamestr + "."
						+ extension;
				try {
					FileUtil.copyFile(new File(localPath), new File(
							newfilepath2));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				String swf1 = "";
				String filetype = filesystem.getStr("filetype");
				int pagenums = 0;
				String flexurl = "";
				if (filetype.equals("文档")) {
					if (new File(newfilepath2).exists()) {
						DocConverter d = new DocConverter(newfilepath2);
						d.conver();
						pagenums = d.getPagenums();
						swf1 = "/appfile/" + filenamestr + "/" + filenamestr
								+ "." + extension;
					}
					String newfilepath3 = newfilepath1 + filenamestr + "."
							+ extension;
					System.out.println("newfilepath31：" + newfilepath3);
					newfilepath3 = newfilepath3.replace("\\", "\\\\");
					System.out.println("newfilepath31：" + newfilepath3);
					if (pagenums == 0)
						flexurl = swf1 + ".swf";
					else
						flexurl = swf1.substring(0, swf1.lastIndexOf("/"))
								+ "/{"
								+ swf1.substring(swf1.lastIndexOf("/") + 1,
										swf1.lastIndexOf(".")) + "[*,1].swf,"
								+ pagenums + "}";
				}

				else if (filetype.equals("图片")) {
					File f1 = new File(localPath);
					String filepath2 = c.getRequest().getRealPath("/")
							+ "/apppic\\" + filenamestr + "." + extension;
					String filepath3 = "/apppic/" + filenamestr + "."
							+ extension;
					System.out.println("filepath2:" + filepath2);
					File f2 = new File(filepath2);
					if (!f2.exists())
						try {
							f2.createNewFile();
							FileUtil.copyFile(f1, f2);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					flexurl = filepath3;
				} else if (filetype.equals("视频")) {
					File f1 = new File(localPath);
					String filepath2 = c.getRequest().getRealPath("/")
							+ "/appvideo\\" + filenamestr + "." + extension;
					String filepath3 = "/appvideo/" + filenamestr + "."
							+ extension;
					System.out.println("filepath2:" + filepath2);
					File f2 = new File(filepath2);
					if (!f2.exists())
						try {
							f2.createNewFile();
							FileUtil.copyFile(f1, f2);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					flexurl = filepath3;
				} else if (filetype.equals("音频")) {
					File f1 = new File(localPath);
					String filepath2 = c.getRequest().getRealPath("/")
							+ "/appmusic\\" + filenamestr + "." + extension;
					String filepath3 = "/appmusic/" + filenamestr + "."
							+ extension;
					System.out.println("filepath2:" + filepath2);
					File f2 = new File(filepath2);
					if (!f2.exists())
						try {
							f2.createNewFile();
							FileUtil.copyFile(f1, f2);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					flexurl = filepath3;
				} else {
					flexurl = "\\" + filepaths;
				}
				flexurl = flexurl;
				System.out.println("flexurl:" + flexurl);
				String win7codecs = "Win7Codecs";
				String xpcodecs = "VistaCodecs";
				c.setAttr("flexurl", flexurl);
				c.setAttr("filename", filename);
				c.setAttr("filetype", filetype);
				c.setAttr("win7codecs", win7codecs);
				c.setAttr("xpcodecs", xpcodecs);
				c.renderJson();
			} else
				c.renderJson("message", "云端文件获取失败！");
		}
	}

	// download 下载云端文件
	public void downloadfile(Controller c, int fileid) {
		FileSystem filesystem = FileSystem.dao.findFirst(
				"select * from filesystem where id=?", fileid);
		if (filesystem == null)
			c.renderJson("message", "文件不存在！");
		else {
			String localPath = c.getRequest().getRealPath("/") + "/download/"
					+ filesystem.getStr("filename");// 设置下载文件存放的临时目录
			System.out.println("临时保存路径：" + localPath);
			if (hdfsoperation.downFromCloud(filesystem.getStr("filepath"),
					localPath)) {// 表示下载文件成功
				File filetemp = new File(localPath);
				c.renderFile(filetemp);
			} else
				c.renderJson("message", "下载文件失败！");
		}
	}

	// 下载视频插件文件
	public void downloadcodecs(Controller c, String filepath) {
		String localPath = c.getRequest().getRealPath("/") + "/download/"
				+ filepath + ".zip";
		File filetemp = new File(localPath);
		c.renderFile(filetemp);
	}

	public String txtRead(File f) {
		String text = "";
		try {
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(fis, "gb2312");// 乱码问题
			BufferedReader br = new BufferedReader(isr);
			String str = "";
			while ((str = br.readLine()) != null) {
				text = text + str + "\r\n";
			}
			// System.out.println("txt:"+text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
}
