package com.bryce.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;

/**
 * lucene 方法封装
 */
public class LuceneUtil {
    /**
     * 搜索索引(封装搜索方法)
     * TermQuery	不使用分析器, 对关键词做精确匹配搜索. 如:订单编号、身份证号
     * NumericRangeQuery	数字范围查询, 比如: 图书价格大于80, 小于100
     * BooleanQuery	布尔查询, 实现组合条件查询. 组合关系有:
     * 1. MUST与MUST: 表示“与”, 即“交集”
     * 2. MUST与MUST NOT: 包含前者, 排除后者
     * 3. MUST NOT与MUST NOT: 没有意义
     * 4. SHOULD与MUST: 表示MUST, SHOULD失去意义
     * 5. SHOULD与MUST NOT: 等于MUST与MUST NOT
     * 6. SHOULD与SHOULD表示“或”, 即“并集”
     */
    public static void seracher(Query query) throws Exception {
        // 打印查询语法
        System.out.println("查询语法: " + query);

        // 1.创建索引库目录位置对象(Directory), 指定索引库的位置
        Directory directory = FSDirectory.open(new File("/tmp/lucene/index2"));

        // 2.创建索引读取对象(IndexReader), 用于读取索引
        IndexReader reader = DirectoryReader.open(directory);

        // 3.创建索引搜索对象(IndexSearcher), 用于执行搜索
        IndexSearcher searcher = new IndexSearcher(reader);

        // 4. 使用IndexSearcher对象执行搜索, 返回搜索结果集TopDocs
        // 参数一:使用的查询对象, 参数二:指定要返回的搜索结果排序后的前n个
        TopDocs topDocs = searcher.search(query, 10);

        // 5. 处理结果集
        // 5.1 打印实际查询到的结果数量
        System.out.println("实际查询到的结果数量: " + topDocs.totalHits);
        // 5.2 获取搜索的结果数组
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

        // 6. 关闭资源
        reader.close();
    }
}
