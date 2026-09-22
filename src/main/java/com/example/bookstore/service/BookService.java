package com.example.bookstore.service;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    public static final int PAGE_SIZE = 8;

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Book> findById(long idBook) {
        return bookRepository.findById(idBook);
    }

    @Transactional(readOnly = true)
    public List<Book> findAll(String search, String category, String language,
                              java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice) {
        return bookRepository.filter(normalize(search), normalize(category), normalize(language), minPrice, maxPrice);
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }

    public Book create(Book book) {
        return bookRepository.save(book);
    }

    public boolean update(Book book) {
        if (!bookRepository.existsById(book.getIdBook())) {
            return false;
        }
        bookRepository.save(book);
        return true;
    }

    public boolean delete(long idBook) {
        if (!bookRepository.existsById(idBook)) {
            return false;
        }
        bookRepository.deleteById(idBook);
        return true;
    }
}
