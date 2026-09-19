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
                        @RequestParam(required = false) Integer page,
                        Model model) {

        if ("new".equals(action)) {
            populateList(model, search, page);
            model.addAttribute("modalBook", new Book());
            model.addAttribute("formTitle", "Create Book");
            return "books";
        }

        if ("edit".equals(action) && id != null) {
            Optional<Book> book = bookService.findById(id);
            populateList(model, search, page);
            if (book.isPresent()) {
                model.addAttribute("modalBook", book.get());
                model.addAttribute("formTitle", "Update Book");
            }
            return "books";
        }

        populateList(model, search, page);
        return "books";
    }

    @PostMapping("/books")
    public String submit(@RequestParam String action,
                          @RequestParam(required = false) Long idBook,
                          @RequestParam(required = false) String title,
                          @RequestParam(required = false) String author,
                          @RequestParam(required = false) String category,
                          @RequestParam(required = false) BigDecimal price,
                          @RequestParam(required = false) Integer stock) {

        switch (action) {
            case "create" -> bookService.create(new Book(title, author, category, price, stock));
            case "update" -> {
                Book book = new Book(idBook, title, author, category, price, stock);
                bookService.update(book);
            }
            case "delete" -> bookService.delete(idBook);
            default -> { }
        }
        return "redirect:/books";
    }

    private void populateList(Model model, String search, Integer page) {
        List<Book> allBooks = bookService.findAll(search);
        int totalPages = Math.max(1, (int) Math.ceil(allBooks.size() / (double) PAGE_SIZE));
        int currentPage = Math.min(Math.max(page == null ? 1 : page, 1), totalPages);

        int fromIndex = Math.min((currentPage - 1) * PAGE_SIZE, allBooks.size());
        int toIndex = Math.min(fromIndex + PAGE_SIZE, allBooks.size());
        List<Book> pageBooks = new ArrayList<>(allBooks.subList(fromIndex, toIndex));

        model.addAttribute("books", pageBooks);
        model.addAttribute("search", search == null ? "" : search);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalBooks", allBooks.size());
    }
}
