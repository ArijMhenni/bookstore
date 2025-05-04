package com.example.bookstore.service;

import com.example.bookstore.models.Book;

import java.util.List;

public interface BookService {
    List<Book> findAllBooks();
    Book getBookById(Long id);
}

