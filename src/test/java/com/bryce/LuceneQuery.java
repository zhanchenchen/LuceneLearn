package com.bryce;

import com.bryce.dao.BookDao;
import com.bryce.dao.impl.BookDaoImpl;
import com.bryce.entity.Book;
import com.bryce.lucene.LuceneUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
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
 * 使用lucene query  api
 */
public class LuceneQuery {

    /**
     * 测试使用QueryParser: 需求: 查询图书名称中包含Lucene, 且包含java的图书
     * Occur.MUST	搜索条件必须满足, 相当于AND	+
     * Occur.SHOULD	搜索条件可选, 相当于OR	空格
     * Occur.MUST_NOT	搜索条件不能满足, 相当于NOT非	-
     *
     * 不支持数值型查询
     */
    @Test
    public void testQueryParser() throws Exception {
        // 1.创建查询对象
        // 1.1.创建分析器对象
        Analyzer analyzer = new IKAnalyzer();
        // 1.2.创建查询解析器对象
        QueryParser qp = new QueryParser("bookName", analyzer);
        // 1.3.使用QueryParser解析查询表达式
//        Query query = qp.parse("bookName:java AND bookName:lucene");
        Query query = qp.parse("bookName:java AND lucene");

        // 2.执行搜索
        LuceneUtil.seracher(query);
    }

    /**
     * 测试使用BooleanQuery: 需求: 查询图书名称中包含Lucene, 且价格在80-100之间的图书
     */
    @Test
    public void testBooleanQuery() throws Exception {
        // 1.创建查询条件
        // 1.1.创建查询条件一
        TermQuery query1 = new TermQuery(new Term("bookName", "lucene"));

        // 1.2.创建查询条件二
        NumericRangeQuery query2 = NumericRangeQuery.newFloatRange("bookPrice", 80f, 100f, true, true);
        // 2.创建组合查询条件
        BooleanQuery bq = new BooleanQuery();
        // add方法: 添加组合的查询条件
        // query参数: 查询条件对象
        // occur参数: 组合条件
        bq.add(query1, BooleanClause.Occur.MUST);
        bq.add(query2, BooleanClause.Occur.MUST);

        // 3.执行搜索
        LuceneUtil.seracher(bq);
    }

    /**
     * 测试使用TermQuery: 需求: 查询图书名称中包含java的图书
     */
    @Test
    public void testTermQuery() throws Exception {
        //1. 创建TermQuery对象
        TermQuery termQuery = new TermQuery(new Term("bookName", "java"));
        // 2.执行搜索
        LuceneUtil.seracher(termQuery);
    }

    /**
     * 测试使用NumericRangeQuery: 需求: 查询图书价格在80-100之间的图书
     */
    @Test
    public void testNumericRangeQuery() throws Exception{
        // 1.创建NumericRangeQuery对象, 参数说明:
        // field: 搜索的域; min: 范围最小值; max: 范围最大值
        // minInclusive: 是否包含最小值(左边界); maxInclusive: 是否包含最大值(右边界)
        NumericRangeQuery numQuery = NumericRangeQuery.newFloatRange("bookPrice", 80f, 100f, true, true);

        // 2.执行搜索
        LuceneUtil.seracher(numQuery);
    }
}