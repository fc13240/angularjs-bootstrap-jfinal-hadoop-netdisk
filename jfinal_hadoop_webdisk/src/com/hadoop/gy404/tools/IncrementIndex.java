package com.hadoop.gy404.tools;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

//增量索引
/*
* 实现思路:首次查询数据库表所有记录，对每条记录建立索引，并将最后一条记录的id存储到storeId.txt文件中
*         当新插入一条记录时，再建立索引时不必再对所有数据重新建一遍索引，
*         可根据存放在storeId.txt文件中的id查出新插入的数据，只对新增的数据新建索引，并把新增的索引追加到原来的索引文件中
* */
public class IncrementIndex {

  public static void main(String[] args) {
      try {
          IncrementIndex index = new IncrementIndex();
          String path = "E:\\workspace2\\Test\\lucene_test\\poiIdext";//索引文件的存放路径
          String storeIdPath = "E:\\workspace2\\Test\\lucene_test\\storeId.txt";//存储ID的路径
          String storeId = "";
          Date date1 = new Date();
          storeId = index.getStoreId(storeIdPath);
          ResultSet rs = index.getResult(storeId);
          System.out.println("开始建立索引。。。。");
          index.indexBuilding(path, storeIdPath, rs);
          Date date2 = new Date();
          System.out.println("耗时："+(date2.getTime()-date1.getTime())+"ms");
          storeId = index.getStoreId(storeIdPath);
          System.out.println(storeId);//打印出这次存储起来的ID
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

  public static void buildIndex(String indexFile, String storeIdFile) {
      try {
          String path = indexFile;//索引文件的存放路径
          String storeIdPath = storeIdFile;//存储ID的路径
          String storeId = "";
          storeId = getStoreId(storeIdPath);
          ResultSet rs = getResult(storeId);
          indexBuilding(path, storeIdPath, rs);
          storeId = getStoreId(storeIdPath);
          System.out.println(storeId);//打印出这次存储起来的ID
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

  public static ResultSet getResult(String storeId) throws Exception {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      String url = "jdbc:mysql://localhost:3306/gy404webdisk";
      String userName = "root";
      String password = "root";
      Connection conn = DriverManager.getConnection(url, userName, password);
      Statement stmt = conn.createStatement();
      String sql = "select  * from filesystem";
      ResultSet rs = stmt.executeQuery(sql + " where id > '" + storeId + "'order by id");
      return rs;
  }

  public static boolean indexBuilding(String path, String storeIdPath, ResultSet rs) {
      try {
          Analyzer luceneAnalyzer = new IKAnalyzer();
          // 取得存储起来的ID，以判定是增量索引还是重新索引
          boolean isEmpty = true;
          try {
              File file = new File(storeIdPath);
              if (!file.exists()) {
                  file.createNewFile();
              }
              FileReader fr = new FileReader(storeIdPath);
              BufferedReader br = new BufferedReader(fr);
              if (br.readLine() != null) {
                  isEmpty = false;
              }
              br.close();
              fr.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
          //isEmpty=false表示增量索引
          Directory dic=FSDirectory.open(new File(path)); 
          IndexWriterConfig iwc=new IndexWriterConfig(Version.LUCENE_42, luceneAnalyzer); 
          iwc.setOpenMode(OpenMode.CREATE_OR_APPEND); 
          iwc.setInfoStream(System.out); 
          IndexWriter writer=new IndexWriter(dic,iwc); 

          String storeId = "";
          boolean indexFlag = false;
          
          int id;
          String filename;
          String filepath;
          String filesize;
          String filetype;
          String fileextension;
          int dir;
          
          String filetext;
          
          while (rs.next()) {
              id = rs.getInt("id");
              filepath = rs.getString("filepath") + "";
              filesize = rs.getString("filesize") + "";
              filetype = rs.getString("filetype") + "";
              fileextension = rs.getString("fileextension") + "";
              dir = rs.getInt("dir");
              filename = rs.getString("filename") + "";
              filetext = rs.getString("filetext") + "";
              writer.addDocument(Document(id,filename,filepath,filesize,filetype,fileextension,dir,filetext));
              storeId = String.valueOf(id);//将拿到的id给storeId，这种拿法不合理，这里为了方便
              indexFlag = true;
          }
          writer.forceMerge(1);
          writer.close();
          if (indexFlag) {
              // 将最后一个的ID存到磁盘文件中
              writeStoreId(storeIdPath, storeId);
          }
          return true;
      } catch (Exception e) {
          e.printStackTrace();
          System.out.println("出错了" + e.getClass() + "\n   错误信息为:   " + e.getMessage());
          return false;
      }

  }

  public static Document Document(int id,String filename, String filepath,String filesize,String filetype,String fileextension,int dir,String filetext) {
      Document doc = new Document();
      doc.add(new IntField("id", id, Field.Store.YES));
      doc.add(new TextField("filename", filename, Field.Store.YES));
      doc.add(new TextField("filepath", filepath, Field.Store.YES));
      doc.add(new TextField("filesize", filesize, Field.Store.YES));
      doc.add(new TextField("filetype", filetype, Field.Store.YES));
      doc.add(new TextField("fileextension", fileextension, Field.Store.YES));
      doc.add(new IntField("dir", dir, Field.Store.YES));
      doc.add(new TextField("filetext", filetext, Field.Store.YES));//查询字段
      return doc;
  }

  // 取得存储在磁盘中的ID
  public static String getStoreId(String path) {
      String storeId = "";
      try {
          File file = new File(path);
          if (!file.exists()) {
              file.createNewFile();
          }
          FileReader fr = new FileReader(path);
          BufferedReader br = new BufferedReader(fr);
          storeId = br.readLine();
          if (storeId == null || storeId == "") storeId = "0";
          br.close();
          fr.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
      return storeId;
  }

  // 将ID写入到磁盘文件中
  public static boolean writeStoreId(String path, String storeId) {
      boolean b = false;
      try {
          File file = new File(path);
          if (!file.exists()) {
              file.createNewFile();
          }
          FileWriter fw = new FileWriter(path);
          PrintWriter out = new PrintWriter(fw);
          out.write(storeId);
          out.close();
          fw.close();
          b = true;
      } catch (IOException e) {
          e.printStackTrace();
      }
      return b;
  }
}

