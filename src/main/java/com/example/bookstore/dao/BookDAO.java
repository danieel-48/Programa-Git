package com.example.bookstore.dao;

import com.example.bookstore.model.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Operaciones CRUD para la entidad Book.
 */
public interface BookDAO {

    Book create(Book book) throws SQLException;

    Optional<Book> findById(long idBook) throws SQLException;

    List<Book> findAll(String search) throws SQLException;

    boolean update(Book book) throws SQLException;

    boolean delete(long idBook) throws SQLException;
}
