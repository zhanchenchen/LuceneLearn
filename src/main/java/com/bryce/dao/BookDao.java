package com.bryce.dao;

import com.bryce.entity.Book;

import java.util.List;

public interface BookDao {
    /**
     * 查询全部图书 
     */
    List<Book> listAll();
}