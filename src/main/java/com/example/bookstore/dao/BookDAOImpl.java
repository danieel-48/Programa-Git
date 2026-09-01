package com.example.bookstore.dao;

import com.example.bookstore.db.DatabaseConnection;
import com.example.bookstore.model.Book;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementacion JDBC de BookDAO contra MySQL.
 */
public class BookDAOImpl implements BookDAO {

    private static final String INSERT_SQL =
            "INSERT INTO books (title, author, category, price, stock) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT id_book, title, author, category, price, stock FROM books WHERE id_book = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT id_book, title, author, category, price, stock FROM books ORDER BY id_book";
    private static final String SELECT_SEARCH_SQL =
            "SELECT id_book, title, author, category, price, stock FROM books "
                    + "WHERE CAST(id_book AS CHAR) LIKE ? OR title LIKE ? ORDER BY id_book";
    private static final String UPDATE_SQL =
            "UPDATE books SET title = ?, author = ?, category = ?, price = ?, stock = ? WHERE id_book = ?";
    private static final String DELETE_SQL =
            "DELETE FROM books WHERE id_book = ?";

    @Override
    public Book create(Book book) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            setInsertOrUpdateParams(ps, book);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    book.setIdBook(keys.getLong(1));
                }
            }
            return book;
        }
    }

    @Override
    public Optional<Book> findById(long idBook) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setLong(1, idBook);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Book> findAll(String search) throws SQLException {
        List<Book> books = new ArrayList<>();
        boolean hasSearch = search != null && !search.isBlank();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(hasSearch ? SELECT_SEARCH_SQL : SELECT_ALL_SQL)) {

            if (hasSearch) {
                String like = "%" + search.trim() + "%";
                ps.setString(1, like);
                ps.setString(2, like);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapRow(rs));
                }
            }
        }
        return books;
    }

    @Override
    public boolean update(Book book) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            setInsertOrUpdateParams(ps, book);
            ps.setLong(6, book.getIdBook());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(long idBook) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setLong(1, idBook);
            return ps.executeUpdate() > 0;
        }
    }

    private void setInsertOrUpdateParams(PreparedStatement ps, Book book) throws SQLException {
        ps.setString(1, book.getTitle());
        ps.setString(2, book.getAuthor());
        ps.setString(3, book.getCategory());
        ps.setBigDecimal(4, book.getPrice());
        ps.setInt(5, book.getStock());
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setIdBook(rs.getLong("id_book"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setCategory(rs.getString("category"));
        BigDecimal price = rs.getBigDecimal("price");
        book.setPrice(price);
        book.setStock(rs.getInt("stock"));
        return book;
    }
}
