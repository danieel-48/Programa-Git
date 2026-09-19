package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE CAST(b.idBook AS string) LIKE CONCAT('%', :search, '%') "
            + "OR LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')) ORDER BY b.idBook")
    List<Book> search(@Param("search") String search);
}
