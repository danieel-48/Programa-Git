package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

        @Query("SELECT b FROM Book b WHERE "
            + "(:search IS NULL OR :search = '' OR "
            + "LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(COALESCE(b.author, '')) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(COALESCE(b.category, '')) LIKE LOWER(CONCAT('%', :search, '%'))) AND "
            + "(:category IS NULL OR :category = '' OR LOWER(b.category) = LOWER(:category)) AND "
            + "(:language IS NULL OR :language = '' OR LOWER(b.language) = LOWER(:language)) AND "
            + "(:minPrice IS NULL OR b.price >= :minPrice) AND "
            + "(:maxPrice IS NULL OR b.price <= :maxPrice) ORDER BY b.idBook")
        List<Book> filter(@Param("search") String search,
                  @Param("category") String category,
                  @Param("language") String language,
                  @Param("minPrice") java.math.BigDecimal minPrice,
                  @Param("maxPrice") java.math.BigDecimal maxPrice);
}
