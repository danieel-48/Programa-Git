package com.example.bookstore.web;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador MVC de libros: lista/busca/pagina por GET, crea/actualiza/elimina por POST.
 */
@Controller
public class BookController {

    private static final int PAGE_SIZE = BookService.PAGE_SIZE;

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public String list(@RequestParam(required = false) String action,
                        @RequestParam(required = false) Long id,
                        @RequestParam(required = false) String search,
                        @RequestParam(required = false) String category,
                        @RequestParam(required = false) String language,
                        @RequestParam(required = false) BigDecimal minPrice,
                        @RequestParam(required = false) BigDecimal maxPrice,
                        @RequestParam(required = false) Integer page,
                        Model model) {

        if ("detail".equals(action) && id != null) {
            Optional<Book> book = bookService.findById(id);
            populateList(model, search, category, language, minPrice, maxPrice, page);
            book.ifPresent(value -> model.addAttribute("detailBook", value));
            return "books";
        }

        if ("new".equals(action)) {
            populateList(model, search, category, language, minPrice, maxPrice, page);
            model.addAttribute("modalBook", new Book());
            model.addAttribute("formTitle", "Create Book");
            return "books";
        }

        if ("edit".equals(action) && id != null) {
            Optional<Book> book = bookService.findById(id);
            populateList(model, search, category, language, minPrice, maxPrice, page);
            if (book.isPresent()) {
                model.addAttribute("modalBook", book.get());
                model.addAttribute("formTitle", "Update Book");
            }
            return "books";
        }

        populateList(model, search, category, language, minPrice, maxPrice, page);
        return "books";
    }

    @PostMapping("/books")
    public String submit(@RequestParam String action,
                          @RequestParam(required = false) Long idBook,
                          @RequestParam(required = false) String title,
                          @RequestParam(required = false) String author,
                          @RequestParam(required = false) String category,
                          @RequestParam(required = false) String language,
                          @RequestParam(required = false) BigDecimal price,
                          @RequestParam(required = false) Integer stock) {

        if ("create".equals(action) || "update".equals(action)) {
            String validationError = getValidationError(author, category, language);
            if (validationError != null) {
                    String target = "update".equals(action) && idBook != null
                        ? "/books?action=edit&id=" + idBook
                        : "/books?action=new";
                return "redirect:" + target + "&error="
                        + URLEncoder.encode(validationError, StandardCharsets.UTF_8);
            }
        }

        switch (action) {
            case "create" -> bookService.create(new Book(title, author, category, language, price, stock));
            case "update" -> {
                Book book = new Book(idBook, title, author, category, language, price, stock);
                bookService.update(book);
            }
            case "delete" -> bookService.delete(idBook);
            default -> { }
        }
        return "redirect:/books";
    }

    private String getValidationError(String author, String category, String language) {
        if (author == null || author.isBlank()) {
            return "Author is required and cannot be blank";
        }
        if (category == null || category.isBlank()) {
            return "Category is required and cannot be blank";
        }
        if (language == null || language.isBlank() || language.matches(".*\\d.*")) {
            return "Language is required and cannot contain numbers";
        }
        return null;
    }

    private void populateList(Model model, String search, String category, String language,
                              BigDecimal minPrice, BigDecimal maxPrice, Integer page) {
        List<Book> allBooks = bookService.findAll(search, category, language, minPrice, maxPrice);
        int totalPages = Math.max(1, (int) Math.ceil(allBooks.size() / (double) PAGE_SIZE));
        int currentPage = Math.min(Math.max(page == null ? 1 : page, 1), totalPages);

        int fromIndex = Math.min((currentPage - 1) * PAGE_SIZE, allBooks.size());
        int toIndex = Math.min(fromIndex + PAGE_SIZE, allBooks.size());
        List<Book> pageBooks = new ArrayList<>(allBooks.subList(fromIndex, toIndex));

        model.addAttribute("books", pageBooks);
        model.addAttribute("search", search == null ? "" : search);
        model.addAttribute("category", category == null ? "" : category);
        model.addAttribute("language", language == null ? "" : language);
        model.addAttribute("minPrice", minPrice == null ? "" : minPrice);
        model.addAttribute("maxPrice", maxPrice == null ? "" : maxPrice);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalBooks", allBooks.size());
    }
}
