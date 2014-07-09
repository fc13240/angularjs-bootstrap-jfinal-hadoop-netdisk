/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* * 
 * doc docx格式转换 
 * 作者:方超
 * 日期：2012-03-11
 * */
package com.hadoop.gy404.tools;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import ooo.connector.BootstrapSocketConnector;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.document.DocumentFamily;
import org.artofsolving.jodconverter.document.DocumentFormat;
import org.artofsolving.jodconverter.document.DocumentFormatRegistry;
import org.artofsolving.jodconverter.document.JsonDocumentFormatRegistry;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeConnectionProtocol;
import org.artofsolving.jodconverter.office.OfficeManager;


import com.lowagie.text.pdf.PdfReader;
import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XCloseable;

public class DocConverter 
{
	private int environment = 0;
	// 环境1：windows 2:linux(涉及pdf2swf路径问题)
	private String pdf2swfpath="";
	private String languagedir="";
	private String fileString;
	private String outputPath = "";
	// 输入路径，如果不设置就输出在默认位置
	private String fileName;
	private File pdfFile;
	private File swfFile;
	private File docFile;
	private String OFFICE_HOME="";
	private OfficeManager officeManager;
	private int pagenums=0;
	private int port[] = {8100};
	//private DocumentFormatRegistry documentFormatRegistry = null;
	//private DocumentFormat customPdfFormat =null;

	public DocConverter(String fileString) 
	{
		ini(fileString);
	}

	/* * 重新设置 file * @param fileString */
	public void setFile(String fileString) 
	{
		ini(fileString);
	}

	
	public int getPagenums() {
		return pagenums;
	}

	public void setPagenums(int pagenums) {
		this.pagenums = pagenums;
	}

	/* * 初始化 * @param fileString */
	private void ini(String fileString) 
	{
		ResourceBundle resource = ResourceBundle.getBundle("pdf2swf");
		environment=Integer.parseInt(resource.getString("environment"));
		pdf2swfpath=resource.getString("path");
		languagedir=resource.getString("languagedir");
		OFFICE_HOME=resource.getString("OFFICEHOME");
		//this.customPdfFormat=customPdfFormat;
		this.fileString = fileString;
		fileName = fileString.substring(0, fileString.lastIndexOf("."));
		String extension=fileString.substring(fileString.lastIndexOf(".")+1);
		if(extension.equalsIgnoreCase("txt"))
		{
			Charset fileCharset = FileUtil.getFileEncoding(new File(fileString));
            if (fileCharset != null) {
                Charset systemCharset = Charset.defaultCharset();
                if (!fileCharset.equals(systemCharset) && !(systemCharset.equals(Charset.forName("GBK"))
                        && fileCharset.name().toLowerCase().equals("gb2312"))) {
                    String encodedFileName = FileUtil.getFilePrefix(new File(fileString).getPath()) + "_encoded." + (environment ==1 ? "odt" : "txt");
                    File encodedFile = new File(encodedFileName);
                    try {
                        FileUtil.convertFileEncodingToSys(new File(fileString), encodedFile);
                    } catch (Exception e) {
                        try {
							org.apache.commons.io.FileUtils.copyFile(new File(fileString), encodedFile);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    }
                    docFile = encodedFile;
                } else if (environment == 1) {
                    String encodedFileName = FileUtil.getFilePrefix(new File(fileString).getPath()) + "_encoded.odt";
                    File encodedFile = new File(encodedFileName);
                    try {
						org.apache.commons.io.FileUtils.copyFile(new File(fileString), encodedFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    docFile = encodedFile;
                }
            }
            docFile = new File(fileName + ".odt");
			try {
				FileUtil.copyFile(new File(fileString), docFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		}
		else
			docFile = new File(fileString);
		pdfFile = new File(fileName + ".pdf");
		swfFile = new File(fileName + "%.swf");
	}

	/* * 转为PDF * @param file */
	private void doc2pdf() throws Exception 
	{
		if (docFile.exists()) 
		{
			if (!pdfFile.exists()) 
			{
				String convertType = "writer_pdf_Export";
				System.out.println("进行文档转换转换:" + docFile.getPath() + " --> " + pdfFile.getPath());
				try{
		        	System.out.println("docFile:"+docFile.getPath());
		        	XComponentContext context = createContext();
			        System.out.println("连接office ...");
			        
			        XComponentLoader compLoader = createLoader(context);
			        System.out.println("创建加载对象 ...");
			        
			        Object doc = loadDocument(compLoader, docFile.getPath());
			        System.out.println("加载文档...");
			        
			        convertDocument(doc, pdfFile.getPath(), convertType);
			        System.out.println("转换文档 ...");
			        
			        closeDocument(doc);
			        System.out.println("关闭文档 ...");
		        }
		        catch(Exception ex)
		        {
		        	ex.printStackTrace();
		        }
				//OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
		       // if (customPdfFormat!=null)
				//	converter.convert(docFile,pdfFile, customPdfFormat);
				//else 
				//	converter.convert(docFile,pdfFile);
				/*OpenOfficeConnection connection = new SocketOpenOfficeConnection("localhost",8100);
				try 
				{
					connection.connect();
					DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
					converter.convert(docFile, pdfFile);
					// close the connection
					connection.disconnect();
					System.out.println("****pdf转换成功，PDF输出：" + pdfFile.getPath()+ "****");
				} 
				catch (java.net.ConnectException e) 
				{ 
					e.printStackTrace();
					System.out.println("****swf转换异常，openoffice服务未启动！****");
					throw e;
				} 
				catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) 
				{
					e.printStackTrace();
					System.out.println("****swf转换器异常，读取转换文件失败****");
					throw e;
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					throw e;
				}*/
			} 
			else 
			{
				System.out.println("****已经转换为pdf，不需要再进行转化****");
			}
		} 
		else 
		{
			System.out.println("****swf转换器异常，需要转换的文档不存在，无法转换****");
		}
	} /* * 转换成swf */

	private void pdf2swf() throws Exception 
	{
		Runtime r = Runtime.getRuntime();
		if (!swfFile.exists()) 
		{
			if (pdfFile.exists()) 
			{
				if (environment == 1)// windows环境处理
				{
					try 
					{
						String cmdstr="\"" +pdf2swfpath + "\""+ " " + "\"" + pdfFile.getPath() + "\""+ " -o " + "\"" + swfFile.getPath()+ "\""+ " -f -T 9 -t -s poly2bitmap -s storeallcharacters -s languagedir="+"\"" +languagedir+"\"";
						System.out.println(cmdstr);
						Process p = r.exec(cmdstr);
						System.out.print(loadStream(p.getInputStream()));
						System.err.print(loadStream(p.getErrorStream()));
						System.out.print(loadStream(p.getInputStream()));
						System.err.println("****swf转换成功，文件输出："+ swfFile.getPath() + "****");
						if (pdfFile.exists()) 
						{
							pdfFile.delete();
						}
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
						throw e;
					}
				} 
				else if (environment == 2)// linux环境处理
				{
					try 
					{
						Process p = r.exec("pdf2swf " + pdfFile.getPath()+ " -o " + swfFile.getPath()+ " -f -T 9 -t -s storeallcharacters");
						System.out.print(loadStream(p.getInputStream()));
						System.err.print(loadStream(p.getErrorStream()));
						System.err.println("****swf转换成功，文件输出："+ swfFile.getPath() + "****");
						if (pdfFile.exists()) 
						{
							pdfFile.delete();
						}
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
						throw e;
					}
				}
			} 
			else
			{
				System.out.println("****pdf不存在，无法转换****");
			}
		}
		else
		{
			System.out.println("****swf已存在不需要转换****");
		}
	}

	public void getpdfnumbers()
	{
		try {
			this.pagenums= new PdfReader(pdfFile.getAbsolutePath()).getNumberOfPages();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.pagenums=0;
			e.printStackTrace();
		}
	}
	
	static String loadStream(InputStream in) throws IOException 
	{
		int ptr = 0;
		in = new BufferedInputStream(in);
		StringBuffer buffer = new StringBuffer();
		while ((ptr = in.read()) != -1) 
		{
			buffer.append((char) ptr);
		}
		return buffer.toString();
	}

	/* * 转换主方法 */
	public boolean conver() 
	{
		if (swfFile.exists()) 
		{
			System.out.println("****swf转换器开始工作，该文件已经转换为swf****");
			return true;
		}
		if (environment == 1) 
		{
			System.out.println("****swf转换器开始工作，当前设置运行环境windows****");
		} 
		else 
		{
			System.out.println("****swf转换器开始工作，当前设置运行环境linux****");
		}
		try 
		{
			doc2pdf();
			getpdfnumbers();
			pdf2swf();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
		if (swfFile.exists()) 
		{
			return true;
		} 
		else 
		{
			return false;
		}
	}

	/* * 返回文件路径 * @param s */
	public String getswfPath() 
	{
		if (swfFile.exists()) 
		{
			String tempString = swfFile.getPath();
			tempString = tempString.replaceAll("\\\\", "/");
			return tempString;
		} 
		else 
		{
			return "";
		}
	}

	/* * 设置输出路径 */
	public void setOutputPath(String outputPath) 
	{
		this.outputPath = outputPath;
		if (!outputPath.equals("")) 
		{
			String realName = fileName.substring(fileName.lastIndexOf("/"),
					fileName.lastIndexOf("."));
			if (outputPath.charAt(outputPath.length()) == '/') 
			{
				swfFile = new File(outputPath + realName + ".swf");
			} 
			else 
			{
				swfFile = new File(outputPath + realName + ".swf");
			}
		}
	}
	public void startService(){
		DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
        try {
          System.out.println("准备启动服务....");
            configuration.setOfficeHome(OFFICE_HOME);//设置OpenOffice.org安装目录
            configuration.setPortNumbers(port); //设置转换端口，默认为8100
            configuration.setTaskExecutionTimeout(1000 * 60 * 5L);//设置任务执行超时为5分钟
            configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);//设置任务队列超时为24小时        
            configuration.setConnectionProtocol(OfficeConnectionProtocol.SOCKET);
            
            officeManager = configuration.buildOfficeManager();
            officeManager.start();	//启动服务
            System.out.println("office转换服务启动成功!");
        } catch (Exception ce) {
            System.out.println("office转换服务启动失败!详细信息:" + ce);
        }
	}
	
	public void stopService(){
	      System.out.println("关闭office转换服务....");
	        if (officeManager != null) {
	            officeManager.stop();
	        }
	        System.out.println("关闭office转换成功!");
	}
	
	private void openofficeservice()
	{
		Runtime r = Runtime.getRuntime();
		String cmd=OFFICE_HOME+"\\program soffice -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\" -nofirststartwizard ";
		System.out.println(cmd);
		try {
			Process p = r.exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private  XComponentContext createContext() throws Exception {
	    // get the remote office component context
		// String oooExeFolder = "C:/Program Files/OpenOffice 4/program/";
		    return BootstrapSocketConnector.bootstrap(OFFICE_HOME+"\\program\\");
	}
	private  XComponentLoader createLoader(XComponentContext context) throws Exception {
	    // get the remote office service manager
	    XMultiComponentFactory mcf = context.getServiceManager();
	    Object desktop = mcf.createInstanceWithContext("com.sun.star.frame.Desktop", context);
	    return UnoRuntime.queryInterface(XComponentLoader.class, desktop);
	}
	private  Object loadDocument(XComponentLoader loader, String inputFilePath) throws Exception {
	    // Preparing properties for loading the document
	    PropertyValue[] propertyValues = new PropertyValue[1];
	    propertyValues[0] = new PropertyValue();
	    propertyValues[0].Name = "Hidden";
	    propertyValues[0].Value = new Boolean(true);
	    
	    // Composing the URL by replacing all backslashs
	    File inputFile = new File(inputFilePath);
	    String inputUrl = "file:///" + inputFile.getAbsolutePath().replace('\\', '/');
	    System.out.println(inputUrl);
	    return loader.loadComponentFromURL(inputUrl, "_blank", 0, propertyValues);
	}
	private  void convertDocument(Object doc, String outputFilePath, String convertType) throws Exception {
	    // Preparing properties for converting the document
	    PropertyValue[] propertyValues = new PropertyValue[2];
	    // Setting the flag for overwriting
	    propertyValues[0] = new PropertyValue();
	    propertyValues[0].Name = "Overwrite";
	    propertyValues[0].Value = new Boolean(true);
	    // Setting the filter name
	    propertyValues[1] = new PropertyValue();
	    propertyValues[1].Name = "FilterName";
	    propertyValues[1].Value = convertType;
	    
	    // Composing the URL by replacing all backslashs
	    File outputFile = new File(outputFilePath);
	    String outputUrl = "file:///" + outputFile.getAbsolutePath().replace('\\', '/');
	    
	    // Getting an object that will offer a simple way to store
	    // a document to a URL.
	    XStorable storable = UnoRuntime.queryInterface(XStorable.class, doc);
	    // Storing and converting the document
	    storable.storeToURL(outputUrl, propertyValues);
	}
	private  void closeDocument(Object doc) throws Exception {
	    // Closing the converted document. Use XCloseable.clsoe if the
	    // interface is supported, otherwise use XComponent.dispose
	    XCloseable closeable = UnoRuntime.queryInterface(XCloseable.class, doc);
	    
	    if (closeable != null) {
	    	closeable.close(false);
	    } else {
	        XComponent component = UnoRuntime.queryInterface(XComponent.class, doc);
	        component.dispose();
	    }
	}
}
