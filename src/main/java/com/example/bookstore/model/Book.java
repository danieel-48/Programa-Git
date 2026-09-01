package com.example.bookstore.model;

import java.math.BigDecimal;

/**
 * Representa un registro de la tabla books.
 */
public class Book {

    private Long idBook;
    private String title;
    private String author;
    private String category;
    private BigDecimal price;
    private int stock;

    public Book() {
    }

    public Book(String title, String author, String category, BigDecimal price, int stock) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public Book(Long idBook, String title, String author, String category, BigDecimal price, int stock) {
        this.idBook = idBook;
        this.title = title;
        this.author = author;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public Long getIdBook() {
        return idBook;
    }

    public void setIdBook(Long idBook) {
        this.idBook = idBook;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
