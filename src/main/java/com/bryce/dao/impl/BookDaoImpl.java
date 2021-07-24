package com.bryce.dao.impl;

import com.bryce.dao.BookDao;
import com.bryce.entity.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {
    /**
     * 查询全部图书
     */
    public List<Book> listAll() {
        // 创建图书结果集合List
        List<Book> books = new ArrayList<Book>();
        
        Connection conn = null;
        PreparedStatement preStatement = null;
        ResultSet resultSet = null;
        
        try {
            // 加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 创建数据库连接对象
            conn = DriverManager.getConnection(
                       "jdbc:mysql://127.0.0.1:3306/lucene?allowPublicKeyRetrieval=true&useSSL=false",
                       "root", 
                       "mysqladmin");
            
            // 定义查询SQL
            String sql = "select * from book";
            // 创建Statement语句对象
            preStatement = conn.prepareStatement(sql);
            // 执行语句, 得到结果集
            resultSet = preStatement.executeQuery();
            
            // 处理结果集
            while (resultSet.next()) {
                 // 创建图书对象
                 Book book = new Book();
                 book.setId(resultSet.getInt("id"));
                 book.setBookname(resultSet.getString("bookname"));
                 book.setPrice(resultSet.getFloat("price"));
                 book.setPic(resultSet.getString("pic"));
                 book.setBookdesc(resultSet.getString("bookdesc"));
                 // 将查询到的结果添加到list中
                 books.add(book);
            }
       } catch (Exception e) {
            e.printStackTrace();
       } finally {
            // 释放资源
            try {
                 if (null != conn) conn.close();
                 if (null != preStatement) preStatement.close();
                 if (null != resultSet) resultSet.close();
            } catch (Exception e) {
                 e.printStackTrace();
            }
        }
        return books;
    }
    
    /**
     * 测试功能的主方法
     */
    public static void main(String[] args) {
        // 创建图书Dao的实现对象
        BookDao bookDao = new BookDaoImpl();
        List<Book> books = bookDao.listAll();
        
        // 如果结果不为空, 则便利输出
        for (Book book : books) {
            System.out.println(book);
        }
    }
}