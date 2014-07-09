package com.hadoop.gy404.tools;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.surround.parser.ParseException;
import org.apache.lucene.queryparser.surround.parser.QueryParser;
import org.apache.lucene.queryparser.surround.query.SrndQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.hadoop.gy404.model.FileSystem;
import com.hadoop.gy404.model.FileSystem1;

public class IndexUtils {

    //0. 创建增量索引
    public static void buildIndex(String indexFile, String storeIdFile) {
        IncrementIndex.buildIndex(indexFile, storeIdFile);
    }

    //1. 单字段查询
    @SuppressWarnings("deprecation")
    public static List<FileSystem1> queryByOneKey(IndexSearcher indexSearcher, String field,
            String key) {
        try {
        	Date date1 = new Date();
        	BooleanQuery q = new BooleanQuery();        	 
        	q.add(new TermQuery(new Term("filetext", field)), Occur.SHOULD);
        	 TopDocs topDocs = null;
             try {
                 topDocs =indexSearcher.search(q, 10);
             } catch (IOException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
             ScoreDoc[] hits = topDocs.scoreDocs;
            Date date2 = new Date();
            System.out.println("耗时：" + (date2.getTime() - date1.getTime()) + "ms");
            SimpleHTMLFormatter simpleHtmlFormatter = new SimpleHTMLFormatter("<span class=\"hitpoint\">", "</span>");//设定高亮显示的格式，也就是对高亮显示的词组加上前缀后缀
            Highlighter highlighter = new Highlighter(simpleHtmlFormatter, new QueryScorer(q));
            highlighter.setTextFragmenter(new SimpleFragmenter(200));//设置每次返回的字符数.想必大家在使用搜索引擎的时候也没有一并把全部数据展示出来吧，当然这里也是设定只展示部分数据
            Document doc1;
            List<FileSystem1> list = new ArrayList<FileSystem1>();
            for (int i = 0; i < hits.length; i++) {
            	 doc1 = indexSearcher.doc(hits[i].doc);
                 TokenStream tokenStream = null;
                 StopFilter sf = null;

                 try {
                     sf = (StopFilter) new IKAnalyzer().tokenStream("", new StringReader(doc1.get("filetext")));
                 } catch (IOException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                 }
                 FileSystem1 ir = getIndexResult(doc1);
                 try {
                     ir.setFiletext(highlighter.getBestFragment(sf, doc1.get("filetext")));
                 } catch (IOException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                 } catch (InvalidTokenOffsetsException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                 }
                 list.add(ir);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

   /* //2. 多条件查询。这里实现的是and操作
    //注：要查询的字段必须是index的
    //即doc.add(new Field("pid", rs.getString("pid"), Field.Store.YES,Field.Index.TOKENIZED));   
    @SuppressWarnings("deprecation")
    public static List<IndexResult> queryByMultiKeys(IndexSearcher indexSearcher, String[] fields,
            String[] keys) {

        try {
            BooleanQuery m_BooleanQuery = new BooleanQuery();
            if (keys != null && keys.length > 0) {
                for (int i = 0; i < keys.length; i++) {
                    QueryParser queryParser = new QueryParser(fields[i], new StandardAnalyzer());
                    Query query = queryParser.parse(keys[i]);
                    m_BooleanQuery.add(query, BooleanClause.Occur.MUST);//and操作
                }
                Hits hits = indexSearcher.search(m_BooleanQuery);
                List<IndexResult> list = new ArrayList<IndexResult>();
                for (int i = 0; i < hits.length(); i++) {
                    list.add(getIndexResult(hits.doc(i)));
                }
                return list;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //3.高亮显示  实现了单条件查询
    //可改造为多条件查询
    public static List<IndexResult> highlight(IndexSearcher indexSearcher, String key) {
        try {
            QueryParser queryParser = new QueryParser("name", new StandardAnalyzer());
            Query query = queryParser.parse(key);
            TopDocCollector collector = new TopDocCollector(800);
            indexSearcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            Highlighter highlighter = null;
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>",
                    "</font>");
            highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));
            highlighter.setTextFragmenter(new SimpleFragmenter(200));
            List<IndexResult> list = new ArrayList<IndexResult>();
            Document doc;
            for (int i = 0; i < hits.length; i++) {
                //System.out.println(hits[i].score);
                doc = indexSearcher.doc(hits[i].doc);
                TokenStream tokenStream = new StandardAnalyzer().tokenStream("name",
                        new StringReader(doc.get("name")));
                IndexResult ir = getIndexResult(doc);
                ir.setName(highlighter.getBestFragment(tokenStream, doc.get("name")));
                list.add(ir);
            }
            return list;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    //4. 多字段查询
    @SuppressWarnings("deprecation")
    public static List<IndexResult> queryByMultiFileds(IndexSearcher indexSearcher,
            String[] fields, String key) {
        try {
            MultiFieldQueryParser mfq = new MultiFieldQueryParser(fields, new StandardAnalyzer());
            Query query = mfq.parse(key);
            Hits hits = indexSearcher.search(query);
            List<IndexResult> list = new ArrayList<IndexResult>();
            for (int i = 0; i < hits.length(); i++) {
                list.add(getIndexResult(hits.doc(i)));
            }

            return list;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //5. 删除索引
    public static void deleteIndex(String indexFile, String id) throws CorruptIndexException,
            IOException {
        IndexReader indexReader = IndexReader.open(indexFile);
        indexReader.deleteDocuments(new Term("id", id));
        indexReader.close();
    }

    //6. 一元分词
    @SuppressWarnings("deprecation")
    public static String Standard_Analyzer(String str) {
        Analyzer analyzer = new StandardAnalyzer();
        Reader r = new StringReader(str);
        StopFilter sf = (StopFilter) analyzer.tokenStream("", r);
        System.out.println("=====StandardAnalyzer====");
        System.out.println("分析方法：默认没有词只有字（一元分词）");
        Token t;
        String results = "";
        try {
            while ((t = sf.next()) != null) {
                System.out.println(t.termText());
                results = results + " " + t.termText();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    //7. 字典分词
    @SuppressWarnings("deprecation")
    public static String ik_CAnalyzer(String str) {
        Analyzer analyzer = new IK_CAnalyzer();
        Reader r = new StringReader(str);
        TokenStream ts = (TokenStream) analyzer.tokenStream("", r);
        System.out.println("=====IK_CAnalyzer====");
        System.out.println("分析方法:字典分词,正反双向搜索");
        Token t;
        String results = "";
        try {
            while ((t = ts.next()) != null) {
                System.out.println(t.termText());
                results = results + " " + t.termText();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }
*/
    //在结果中搜索
    public static void queryFromResults() {

    }

    //组装对象
    public static FileSystem1 getIndexResult(Document doc) {
    	FileSystem1 ir = new FileSystem1();
    	 ir.setId(Integer.valueOf(doc.get("id")));
        ir.setFilename(doc.get("filename"));
        ir.setFilepath(doc.get("filepath"));
        ir.setFilesize(doc.get("filesize"));
        ir.setFiletype(doc.get("filetype"));
        ir.setFileextension(doc.get("fileextension"));
        ir.setDir(Integer.valueOf(doc.get("dir")));
        ir.setFiletext(doc.get("filetext"));
        return ir;
    }
}

