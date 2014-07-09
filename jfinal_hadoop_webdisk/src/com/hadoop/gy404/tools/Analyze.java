package com.hadoop.gy404.tools;


import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.bitform.api.FileFormat;
import net.bitform.api.elements.BaseElementHandler;
import net.bitform.api.secure.SecureOptions;
import net.bitform.api.secure.SecureRequest;
import net.bitform.api.secure.SecureResponse;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Analyze {

	private  String text="";
	private  String propname="";
	private  String propvalue="";
	
	public String getText() {
		return text;
	}

	public String getpropname() {
		return propname;
	}

	public String getpropvalue() {
		return propvalue;
	}
	
	
	//抽取文件内容
	public void textExtract(File f){
		SecureRequest request = new SecureRequest();
	    request.setOption(SecureOptions.JustAnalyze, true);
	    request.setOption(SecureOptions.OutputType, SecureOptions.OutputTypeOption.ToHandler);       
	    //其他文件格式的支持，使用OutsideIn
	    request.setOption(SecureOptions.UseOutsideIn,SecureOptions.UseOutsideInOption.Standard);
	    request.setOption(SecureOptions.EmbeddingExportList, new FileFormat[0]);
	    request.setOption(SecureOptions.ElementHandler, new MyHandler());
	    request.setOption(SecureOptions.SourceDocument, f);
	    try{
	    	
	    	// Execute the request
            request.execute();
            // Get the response
            SecureResponse response = request.getResponse();

            if (response.getResult(SecureOptions.WasProcessed)) {
            	//System.out.println("The file has a format of " + response.getResult(SecureOptions.SourceFormat).getName());     
            } else {
                System.out.println("The file " + f.getName() + " of format " + response.getResult(SecureOptions.SourceFormat).getName() + " could not be processed.");
            }
            
	    }catch (IOException e) {
            System.out.println("Exception in file " + f.getName());
            e.printStackTrace();
        } catch (RuntimeException e) {
            System.out.println("Exception in file " + f.getName());
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            System.out.println("Exception in file " + f.getName());
            e.printStackTrace();
        } catch (StackOverflowError e) {
            System.out.println("Exception in file " + f.getName());
            e.printStackTrace();
        }
	}
	//MyHandler模式
	class MyHandler extends BaseElementHandler {
		public void text(CharBuffer buffer) throws IOException {
            //System.out.println(buffer.toString());
            text=text+buffer.toString()+"\r\n";
        }
	}
	
	//抽取文件属性名和属性值
	public void propExtract(File f){
		boolean flag=true;
		String filepath=f.getAbsolutePath();
		String xmlpath=filepath+".xml";
		File f1=new File(xmlpath);
		SecureRequest request = new SecureRequest();
		request.setOption(SecureOptions.JustAnalyze, true);
	    request.setOption(SecureOptions.OutputType, SecureOptions.OutputTypeOption.ToXML);       
	    //其他文件格式的支持，使用OutsideIn
	    request.setOption(SecureOptions.UseOutsideIn,SecureOptions.UseOutsideInOption.Standard);
	    request.setOption(SecureOptions.EmbeddingExportList, new FileFormat[0]);
	    request.setOption(SecureOptions.ResultDocument, f1);	    
	    //request.setOption(SecureOptions.ElementHandler, new MyHandler());
	    request.setOption(SecureOptions.SourceDocument, f);
	    try{
	    	
	    	// Execute the request
            request.execute();
            // Get the response
            SecureResponse response = request.getResponse();

            if (response.getResult(SecureOptions.WasProcessed)) {
            	System.out.println("The file has a format of " + response.getResult(SecureOptions.SourceFormat).getName());     
            } else {
                flag=false;
            	System.out.println("The file " + f.getName() + " of format " + response.getResult(SecureOptions.SourceFormat).getName() + " could not be processed.");
            }
            
	    }catch (IOException e) {
            System.out.println("Exception in file " + f.getName());
            e.printStackTrace();
        } catch (RuntimeException e) {
            System.out.println("Exception in file " + f.getName());
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            System.out.println("Exception in file " + f.getName());
            e.printStackTrace();
        } catch (StackOverflowError e) {
            System.out.println("Exception in file " + f.getName());
            e.printStackTrace();
        }
	    if(flag){
		    this.xmlParse(f1);
		    f1.delete();
		    System.out.println("Done!");
	    }
	}
	
	//解析xml文件，获取属性名和属性值
	public void xmlParse(File f){

		HashMap map1=null;
		HashMap map=null;
		List list=null;
		Iterator iter=null;
		String contentStatus,contentType,manager,category,author,title,template,lastsavedby,subject,keywords,company,comments;
		String imagedes,make,model,artist,copyright,comment,revisiondate,creator,
			id3_tpe1,id3_tit2,id3_talb,id3_tyer,id3_tpe2,id3_tcon,id3_TPUB,id3_TIT3,width,heigth,xdimension,ydimension;
		String revisionnumber,creatingapp,datelastsaved,pagecount,wordcount,pretarget,bytecount,paragraphcount,slidecount,charactercount,
			datecreated,security,totaleditingtime,linecount,appversion,scalecrop,linksUpToDate,sharedDocument,hyperlinksChanged,hiddenSlideCount,multimediaClipCount,lastPrinted;
		
		try {
			SAXReader reader=new SAXReader();
			Document doc=reader.read(f);
			//对于带有命名空间的xml文件，所有XPath节点前都需要加上命名空间
			map1=new HashMap<String,String>();
			map=new HashMap<String,String>();
			map1.put("ns", "http://www.bitform.net/xml/schema/elements.xsd");
			reader.getDocumentFactory().setXPathNamespaceURIs(map1);
			
			//SummaryProperties:author,title,template,lastsavedby,subject,keywords,company
			list=doc.selectNodes("//ns:stringproperty [@id='35127323']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute contentStatus1=(Attribute)iter.next();
				contentStatus=contentStatus1.getText();
				map.put("ContentStatus", contentStatus);
			}
			
			list=doc.selectNodes("//ns:stringproperty [@id='35127322']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute contentType1=(Attribute)iter.next();
				contentType=contentType1.getText();
				map.put("ContentType", contentType);
			}
			
			list=doc.selectNodes("//ns:textproperty[@id='34668558']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element manager1=(Element)iter.next();
				manager=manager1.getText();
				map.put("Manager", manager);
			}
			
			list=doc.selectNodes("//ns:textproperty[@id='34668546']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element category1=(Element)iter.next();
				category=category1.getText();
				map.put("Category", category);
			}
			
			
			
			list=doc.selectNodes("//ns:textproperty[@id='22085636']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element author1=(Element)iter.next();
				author=author1.getText();
				map.put("Author（作者）", author);
			}
			
			list=doc.selectNodes("//ns:textproperty[@id='17891330']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element title1=(Element)iter.next();
				title=title1.getText();
				map.put("Title（标题）", title);
			}
			
			list=doc.selectNodes("//ns:textproperty[@id='18350087']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element template1=(Element)iter.next();
				template=template1.getText();
				map.put("Template（模板）", template);
			}
			
			list=doc.selectNodes("//ns:textproperty[@id='23134216']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element lastsavedby1=(Element)iter.next();
				lastsavedby=lastsavedby1.getText();
				map.put("LastSavedBy（最后保存者）", lastsavedby);
			}
			
			list=doc.selectNodes("//ns:textproperty[@id='17891331']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element subject1=(Element)iter.next();
				subject=subject1.getText();
				map.put("Subject（主题）", subject);
			}
			
			list=doc.selectNodes("//ns:textproperty[@id='17891333']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element keywords1=(Element)iter.next();
				keywords=keywords1.getText();
				map.put("Keywords（关键字）", keywords);
			}
			
			list=doc.selectNodes("//ns:textproperty[@id='34668559']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element company1=(Element)iter.next();
				company=company1.getText();
				map.put("Company（公司）", company);
			}
			
			list=doc.selectNodes("//ns:textproperty[@id='17891334']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element comments1=(Element)iter.next();
				comments=comments1.getText();
				map.put("Comments", comments);
			}
			
			/*CustomProperties:imagedes,make,model,artist,copyright,title,comment,author,keywords,subject,company,revisiondate,creator,
			id3_tpe1,id3_tit2,id3_talb,id3_tyer,id3_tpe2,width,heigth*/
			list=doc.selectNodes("//ns:textproperty[name='ImageDescription']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element imagedes1=(Element)iter.next();
				imagedes=imagedes1.getText();
				map.put("ImageDescription（图片描述）", imagedes);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='Make']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element make1=(Element)iter.next();
				make=make1.getText();
				map.put("Make(相机制造商)", make);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='Model']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element model1=(Element)iter.next();
				model=model1.getText();
				map.put("Model（相机型号）", model);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='Artist']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element artist1=(Element)iter.next();
				artist=artist1.getText();
				map.put("Artist", artist);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='Copyright']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element copyright1=(Element)iter.next();
				copyright=copyright1.getText();
				map.put("Copyright", copyright);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='Title']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element title1=(Element)iter.next();
				title=title1.getText();
				map.put("Title（标题）", title);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='Comment']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element comment1=(Element)iter.next();
				comment=comment1.getText();
				map.put("Comment", comment);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='Author']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element author1=(Element)iter.next();
				author=author1.getText();
				map.put("Author（作者）", author);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='Keywords']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element keywords1=(Element)iter.next();
				keywords=keywords1.getText();
				map.put("Keywords（关键字）", keywords);
			}
			
			list=doc.selectNodes("//ns:textproperty[name='Subject']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element subject1=(Element)iter.next();
				subject=subject1.getText();
				map.put("Subject（主题）", subject);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='Company']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element company1=(Element)iter.next();
				company=company1.getText();
				map.put("Company", company);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='RevisionDate']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element revisiondate1=(Element)iter.next();
				revisiondate=revisiondate1.getText();
				map.put("RevisionDate（修订日期）", revisiondate);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='Creator']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element creator1=(Element)iter.next();
				creator=creator1.getText();
				map.put("Creator", creator);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='ID3_TPE1']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element id3_tpe11=(Element)iter.next();
				id3_tpe1=id3_tpe11.getText();
				map.put("ID3_TPE1(作者)", id3_tpe1);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='ID3_TIT2']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element id3_tit21=(Element)iter.next();
				id3_tit2=id3_tit21.getText();
				map.put("ID3_TIT2（标题）", id3_tit2);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='ID3_TALB']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element id3_talb1=(Element)iter.next();
				id3_talb=id3_talb1.getText();
				map.put("ID3_TALB（唱片集）", id3_talb);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='ID3_TYER']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element id3_tyer1=(Element)iter.next();
				id3_tyer=id3_tyer1.getText();
				map.put("ID3_TYER（发行年份）", id3_tyer);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='ID3_TPE2']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element id3_tpe21=(Element)iter.next();
				id3_tpe2=id3_tpe21.getText();
				map.put("ID3_TPE2(唱片集艺术家)", id3_tpe2);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='ID3_TCON']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element id3_tcon1=(Element)iter.next();
				id3_tcon=id3_tcon1.getText();
				map.put("ID3_TCON(流派)", id3_tcon);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='ID3_TPUB']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element id3_TPUB1=(Element)iter.next();
				id3_TPUB=id3_TPUB1.getText();
				map.put("ID3_TPUB(发布者)", id3_TPUB);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='ID3_TIT3']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element id3_TIT31=(Element)iter.next();
				id3_TIT3=id3_TIT31.getText();
				map.put("ID3_TIT3(副标题)", id3_TIT3);
			}
			
			list=doc.selectNodes("//ns:textproperty[@id='75739']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element width1=(Element)iter.next();
				width=width1.getText();
				map.put("Width(帧宽度)", width);
			}
			
			list=doc.selectNodes("//ns:textproperty[@id='75740']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element heigth1=(Element)iter.next();
				heigth=heigth1.getText();
				map.put("Heigth（帧高度）", heigth);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='PixelXDimension']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element xdimension1=(Element)iter.next();
				xdimension=xdimension1.getText();
				map.put("Xdimension（宽度）", xdimension);
			}
			
			list=doc.selectNodes("//ns:textproperty[@name='PixelYDimension']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element ydimension1=(Element)iter.next();
				ydimension=ydimension1.getText();
				map.put("Ydimension（高度）", ydimension);
			}
			
			//StatisticProperties:revisionnumber,creatingapp,datelastsaved,pagecount,wordcount,pretarget,bytecount,paragraphcount,slidecount
			list=doc.selectNodes("//ns:textproperty[@id='19398665']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element revisionnumber1=(Element)iter.next();
				revisionnumber=revisionnumber1.getText();
				map.put("RevisionNumber(修订号)", revisionnumber);
			}
			
			list=doc.selectNodes("//ns:stringproperty [@id='19398665']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute revisionnumber1=(Attribute)iter.next();
				revisionnumber=revisionnumber1.getText();
				map.put("RevisionNumber(修订号)", revisionnumber);
			}
			
			list=doc.selectNodes("//ns:textproperty[@id='19398674']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element creatingapp1=(Element)iter.next();
				creatingapp=creatingapp1.getText();
				map.put("CreatingApplication", creatingapp);
			}
			
			list=doc.selectNodes("//ns:stringproperty [@id='19398674']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute creatingapp1=(Attribute)iter.next();
				creatingapp=creatingapp1.getText();
				map.put("CreatingApplication", creatingapp);
			}
			
			list=doc.selectNodes("//ns:dateproperty[@id='19267597']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute datelastsaved1=(Attribute)iter.next();
				datelastsaved=datelastsaved1.getText();
				//转换时区
				datelastsaved=this.changeDate(datelastsaved);
				map.put("DateLastSaved(最后一次保存的日期)", datelastsaved);
			}
			
			list=doc.selectNodes("//ns:integerproperty[@id='19005454']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute pagecount1=(Attribute)iter.next();
				pagecount=pagecount1.getText();
				map.put("PageCount（页数）", pagecount);
			}
			
			list=doc.selectNodes("//ns:integerproperty[@id='19005455']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute wordcount1=(Attribute)iter.next();
				wordcount=wordcount1.getText();
				map.put("WordCount（字数）", wordcount);
			}
			
			list=doc.selectNodes("//ns:textproperty[@id='36175875']");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Element pretarget1=(Element)iter.next();
				pretarget=pretarget1.getText();
				map.put("PresentationTarget（演示文稿格式）", pretarget);
			}
			
			list=doc.selectNodes("//ns:integerproperty[@id='3038138']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute bytecount1=(Attribute)iter.next();
				bytecount=bytecount1.getText();
				map.put("ByteCount", bytecount);
			}
			
			list=doc.selectNodes("//ns:integerproperty[@id='35782662']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute paragraphcount1=(Attribute)iter.next();
				paragraphcount=paragraphcount1.getText();
				map.put("ParagraphCount", paragraphcount);
			}
			
			list=doc.selectNodes("//ns:integerproperty[@id='35782663']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute slidecount1=(Attribute)iter.next();
				slidecount=slidecount1.getText();
				map.put("SlideCount(幻灯片数)", slidecount);
			}
			
			list=doc.selectNodes("//ns:dateproperty[@id='19267596']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute datecreated1=(Attribute)iter.next();
				datecreated=datecreated1.getText();
				//时区转换
				datecreated=changeDate(datecreated);
				map.put("DateCreated(创建内容的时间)", datecreated);
			}
			
			list=doc.selectNodes("//ns:integerproperty[@id='19005456']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute charactercount1=(Attribute)iter.next();
				charactercount=charactercount1.getText();
				map.put("CharacterCount", charactercount);
			}
			
			list=doc.selectNodes("//ns:integerproperty[@id='19005459']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute security1=(Attribute)iter.next();
				security=security1.getText();
				map.put("Security", security);
			}
			
			list=doc.selectNodes("//ns:durationproperty[@id='19333130']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute totaleditingtime1=(Attribute)iter.next();
				totaleditingtime=totaleditingtime1.getText();
				map.put("TotalEditingTime", totaleditingtime);
			}
			
			list=doc.selectNodes("//ns:integerproperty[@id='35782661']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute linecount1=(Attribute)iter.next();
				linecount=linecount1.getText();
				map.put("LineCount(行数)", linecount);
			}
			
			list=doc.selectNodes("//ns:integerproperty[@id='35848215']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute appversion1=(Attribute)iter.next();
				appversion=appversion1.getText();
				map.put("AppVersion", appversion1);
			}
			
			list=doc.selectNodes("//ns:booleanproperty[@id='35913739']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute scalecrop1=(Attribute)iter.next();
				scalecrop=scalecrop1.getText();
				map.put("ScaleCrop(比例)", scalecrop);
			}
			
			list=doc.selectNodes("//ns:booleanproperty[@id='35913744']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute linksUpToDate1=(Attribute)iter.next();
				linksUpToDate=linksUpToDate1.getText();
				map.put("LinksUpToDate", linksUpToDate);
			}
			
			list=doc.selectNodes("//ns:booleanproperty[@id='35913747']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute sharedDocument1=(Attribute)iter.next();
				sharedDocument=sharedDocument1.getText();
				map.put("SharedDocument(是否共享)", sharedDocument);
			}
			
			list=doc.selectNodes("//ns:booleanproperty[@id='35913747']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute hyperlinksChanged1=(Attribute)iter.next();
				hyperlinksChanged=hyperlinksChanged1.getText();
				map.put("HyperlinksChanged", hyperlinksChanged);
			}
			
			list=doc.selectNodes("//ns:integerproperty[@id='35782665']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute hiddenSlideCount1=(Attribute)iter.next();
				hiddenSlideCount=hiddenSlideCount1.getText();
				map.put("HiddenSlideCount", hiddenSlideCount);
			}
			
			
			list=doc.selectNodes("//ns:integerproperty[@id='35782666']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute multimediaClipCount1=(Attribute)iter.next();
				multimediaClipCount=multimediaClipCount1.getText();
				map.put("MultimediaClipCount", multimediaClipCount);
			}
			
			list=doc.selectNodes("//ns:dateproperty[@id='19267595']/@value");
			iter=list.iterator();
			while(iter.hasNext())
			{
				Attribute lastPrinted1=(Attribute)iter.next();
				lastPrinted=lastPrinted1.getText();
				//时区转换
				lastPrinted=changeDate(lastPrinted);
				map.put("LastPrinted(最后一次打印时间)", lastPrinted);
			}
			
			//从map中分别取出键和值，作为propname 和propvalue
			Iterator iter1=map.keySet().iterator();
			while(iter1.hasNext()){
				String key=iter1.next().toString();
				String value=map.get(key).toString();
				if(!value.equals("")){
					propname=propname+key+"__";
					propvalue=propvalue+value+"__";
				}
			}
			if(!propname.equals("") && !propvalue.equals("")){
				propname=propname.substring(0,propname.length()-2);
				propvalue=propvalue.substring(0,propvalue.length()-2);
			}			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			//System.out.println("error");
			e.printStackTrace();
			
		}		
	}
	
	public String changeDate(String date){
		String str="";
		String str1="";
		String str2="";
		String str3="";		
		try {
			String[] strs=date.split(":");
			str=strs[0];
			str3=str.substring(0,str.length()-2);
			str1=str.substring(str.length()-2,str.length());
			int a=Integer.parseInt(str1);
			a+=8;
			if(a>23){
				a-=24;
			}
			if(a<10){
				str2="0"+String.valueOf(a);
			}
			else{
				str2=String.valueOf(a);
			}
			str=str3+str2+":"+strs[1]+":"+strs[2]+":"+strs[3];
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			str=date;
		}
		return str;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String text="";
		String propname="";
		String propt_value="";
		File f=new File("D:\\mylove.flv");
		Analyze an=new Analyze();
//		an.textExtract(f);
//		text=an.getText();
//		System.out.println(text);
		an.propExtract(f);
		System.out.println(an.getpropname());
		System.out.println(an.getpropvalue());
//		String date="2013-07-10T1:13:59+00:00";
//		System.out.println(an.changeDate(date));
	}
}
