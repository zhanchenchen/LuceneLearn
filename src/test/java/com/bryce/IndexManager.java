package com.bryce;

import com.bryce.dao.BookDao;
import com.bryce.dao.impl.BookDaoImpl;
import com.bryce.entity.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用lucene创建索引，检索
 */
public class IndexManager {
    /**
     * 检索索引(实现分页)
     * @throws Exception
     */
    @Test
    public void searchIndexByPage() throws Exception {
        // 1. 创建分析器对象(Analyzer), 用于分词
        // Analyzer analyzer = new StandardAnalyzer();
        // 使用ik中文分词器
        Analyzer analyzer = new IKAnalyzer();

        // 2. 创建查询对象(Query)
        // bookName:lucene
        // 2.1 创建查询解析器对象
        // 参数一: 指定一个默认的搜索域
        // 参数二: 分析器
        QueryParser queryParser = new QueryParser("bookName", analyzer);
        // 2.2 使用查询解析器对象, 实例化Query对象
        // 参数: 查询表达式
        Query query = queryParser.parse("bookName:lucene");

        // 3. 创建索引库目录位置对象(Directory), 指定索引库的位置
        Directory directory = FSDirectory.open(new File("/tmp/lucene/index"));

        // 4. 创建索引读取对象(IndexReader), 用于读取索引
        IndexReader reader = DirectoryReader.open(directory);

        // 5. 创建索引搜索对象(IndexSearcher), 用于执行搜索
        IndexSearcher searcher = new IndexSearcher(reader);

        // 6. 使用IndexSearcher对象, 执行搜索, 返回搜索结果集TopDocs
        // search方法: 执行搜索
        // 参数一: 查询对象
        // 参数二: n - 指定返回排序以后的搜索结果的前n个
        TopDocs topDocs = searcher.search(query, 10);

        // 7. 处理结果集
        // 7.1 打印实际查询到的结果数量
        System.out.println("实际查询的结果数量: "+topDocs.totalHits);
        // 7.2获取搜索的结果数组
        // ScoreDoc中: 有我们需要的文档的id, 有我们需要的文档的评分
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        // 增加分页实现 ========= start
        // 当前页
        int page = 2;
        // 每页的大小
        int pageSize = 2;
        // 计算记录起始数
        int start = (page - 1) * pageSize;
        // 计算记录终止数 -- 得到数据索引与实际结果数量中的最小值, 防止数组越界
        // 数据索引: start + pageSize
        // 实际的结果数量: scoreDocs.length
        int end = Math.min(start + pageSize, scoreDocs.length);
        // 增加分页实现 ========= end

        for(int i = start; i < end; i++){
            System.out.println("= = = = = = = = = = = = = = = = = = =");
            // 获取文档Id和评分
            int docId = scoreDocs[i].doc;
            float score =scoreDocs[i].score;
            System.out.println("文档Id: " + docId + " , 文档评分: " + score);
            // 根据文档Id, 查询文档数据 -- 相当于关系数据库中根据主键Id查询
            Document doc = searcher.doc(docId);

            System.out.println("图书Id: " + doc.get("bookId"));
            System.out.println("图书名称: " + doc.get("bookName"));
            System.out.println("图书价格: " + doc.get("bookPrice"));
            System.out.println("图书图片: " + doc.get("bookPic"));
            System.out.println("图书描述: " + doc.get("bookDesc"));
        }
        // 8. 释放资源
        reader.close();
    }

    /**
     * 检索索引功能的测试
     * @throws Exception
     */
    @Test
    public void searchIndexTest() throws Exception {
        // 1. 创建分析器对象(Analyzer), 用于分词
//        Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();   // 中文分词器

        // 2. 创建查询对象(Query)
        // 2.1 创建查询解析器对象
        // 参数一:默认的搜索域, 参数二:使用的分析器
        QueryParser queryParser = new QueryParser("bookName", analyzer);
        // 2.2 使用查询解析器对象, 实例化Query对象
        Query query = queryParser.parse("bookName:lucene");

        // 3. 创建索引库目录位置对象(Directory), 指定索引库位置
//        Directory directory = FSDirectory.open(new File("E:\\lucene\\index"));
        Directory directory = FSDirectory.open(new File("/tmp/lucene/index"));

        // 4. 创建索引读取对象(IndexReader), 用于读取索引
        IndexReader indexReader = DirectoryReader.open(directory);

        // 5. 创建索引搜索对象(IndexSearcher), 用于执行索引
        IndexSearcher searcher = new IndexSearcher(indexReader);

        // 6. 使用IndexSearcher对象执行搜索, 返回搜索结果集TopDocs
        // 参数一:使用的查询对象, 参数二:指定要返回的搜索结果排序后的前n个
        TopDocs topDocs = searcher.search(query, 10);

        // 7. 处理结果集
        // 7.1 打印实际查询到的结果数量
        System.out.println("实际查询到的结果数量: " + topDocs.totalHits);
        // 7.2 获取搜索的结果数组
        // ScoreDoc中有文档的id及其评分
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("= = = = = = = = = = = = = = = = = = =");
            // 获取文档的id和评分
            int docId = scoreDoc.doc;
            float score = scoreDoc.score;
            System.out.println("文档id= " + docId + " , 评分= " + score);
            // 根据文档Id, 查询文档数据 -- 相当于关系数据库中根据主键Id查询数据
            Document doc = searcher.doc(docId);
            System.out.println("图书Id: " + doc.get("bookId"));
            System.out.println("图书名称: " + doc.get("bookName"));
            System.out.println("图书价格: " + doc.get("bookPrice"));
            System.out.println("图书图片: " + doc.get("bookPic"));
            System.out.println("图书描述: " + doc.get("bookDesc"));
        }

        // 8. 关闭资源
        indexReader.close();
    }

    /**
     * 创建索引功能的测试
     * @throws Exception
     */
    @Test
    public void createIndexByField() throws IOException{
        // 1. 采集数据
        BookDao bookDao = new BookDaoImpl();
        List<Book> books = bookDao.listAll();

        // 2. 创建文档对象
        List<Document> documents = new ArrayList<Document>();
        for (Book book : books) {
            Document document = new Document();
            // 给文档对象添加域
            // add方法: 把域添加到文档对象中, field参数: 要添加的域
            // TextField: 文本域, 属性name:域的名称, value:域的值, store:指定是否将域值保存到文档中

            // 图书Id --> StringField
            document.add(new StringField("bookId", book.getId() + "", Field.Store.YES));
            // 图书名称 --> TextField
            document.add(new TextField("bookName", book.getBookname(), Field.Store.YES));
            // 图书价格 --> FloatField
            document.add(new FloatField("bookPrice", book.getPrice(), Field.Store.YES));
            // 图书图片 --> StoredField
            document.add(new StoredField("bookPic", book.getPic()));
            // 图书描述 --> TextField
            document.add(new TextField("bookDesc", book.getBookdesc(), Field.Store.NO));

            // 将文档对象添加到文档对象集合中
            documents.add(document);
        }

        // 3. 创建分析器对象(Analyzer), 用于分词
        Analyzer analyzer = new StandardAnalyzer();

        // 4. 创建索引配置对象(IndexWriterConfig), 用于配置Lucene
        // 参数一:当前使用的Lucene版本, 参数二:分析器
        IndexWriterConfig indexConfig = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);

        // 5. 创建索引库目录位置对象(Directory), 指定索引库的存储位置
        File path = new File("/tmp/lucene/index2");
        Directory directory = FSDirectory.open(path);

        // 6. 创建索引写入对象(IndexWriter), 将文档对象写入索引
        IndexWriter indexWriter = new IndexWriter(directory, indexConfig);

        // 7. 使用IndexWriter对象创建索引
        for (Document doc : documents) {
            // addDocement(doc): 将文档对象写入索引库
            indexWriter.addDocument(doc);
        }

        // 8. 释放资源
        indexWriter.close();
    }

    /**
     * 创建索引功能的测试
     * @throws Exception
     */
    @Test
    public void createIndex() throws IOException {
        // 1. 采集数据
        BookDao bookDao = new BookDaoImpl();
        List<Book> books = bookDao.listAll();
        
        // 2. 创建文档对象
        List<Document> documents = new ArrayList<Document>();
        for (Book book : books) {
            Document document = new Document();
            // 给文档对象添加域
            // add方法: 把域添加到文档对象中, field参数: 要添加的域
            // TextField: 文本域, 属性name:域的名称, value:域的值, store:指定是否将域值保存到文档中
            document.add(new TextField("bookId", book.getId() + "", Field.Store.YES));
            document.add(new TextField("bookName", book.getBookname(), Field.Store.YES));
            document.add(new TextField("bookPrice", book.getPrice() + "", Field.Store.YES));
            document.add(new TextField("bookPic", book.getPic(), Field.Store.YES));
            document.add(new TextField("bookDesc", book.getBookdesc(), Field.Store.YES));

            // 将文档对象添加到文档对象集合中
            documents.add(document);
        }
        // 3. 创建分析器对象(Analyzer), 用于分词
//        Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();   // 中文分词器

        // 4. 创建索引配置对象(IndexWriterConfig), 用于配置Lucene
        // 参数一:当前使用的Lucene版本, 参数二:分析器
        IndexWriterConfig indexConfig = new IndexWriterConfig(Version.LUCENE_4_10_4, analyzer);
        // 5. 创建索引库目录位置对象(Directory), 指定索引库的存储位置
//        File path = new File("E:\\lucene\\index");
        File path = new File("/tmp/lucene/index");
        Directory directory = FSDirectory.open(path);
        // 6. 创建索引写入对象(IndexWriter), 将文档对象写入索引
        IndexWriter indexWriter = new IndexWriter(directory, indexConfig);
        // 7. 使用IndexWriter对象创建索引
        for (Document doc : documents) {
            // addDocement(doc): 将文档对象写入索引库
            indexWriter.addDocument(doc);
        }
        // 8. 释放资源
        indexWriter.close();
    }
}