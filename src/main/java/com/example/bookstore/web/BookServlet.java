package com.example.bookstore.web;

import com.example.bookstore.dao.BookDAO;
import com.example.bookstore.dao.BookDAOImpl;
import com.example.bookstore.model.Book;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador CRUD de libros: lista/busca/pagina por GET, crea/actualiza/elimina por POST.
 */
@WebServlet("/books")
public class BookServlet extends HttpServlet {

    private static final int PAGE_SIZE = 8;

    private final BookDAO bookDAO = new BookDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("new".equals(action)) {
                showList(request, response, new Book(), "Create Book");
                return;
            }

            if ("edit".equals(action)) {
                long id = Long.parseLong(request.getParameter("id"));
                Optional<Book> book = bookDAO.findById(id);
                if (book.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/books");
                    return;
                }
                showList(request, response, book.get(), "Update Book");
                return;
            }

            showList(request, response, null, null);
        } catch (SQLException e) {
            throw new ServletException("Error al acceder a la base de datos", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action == null ? "" : action) {
                case "create" -> {
                    bookDAO.create(readBookFromRequest(request));
                    response.sendRedirect(request.getContextPath() + "/books");
                }
                case "update" -> {
                    Book book = readBookFromRequest(request);
                    book.setIdBook(Long.parseLong(request.getParameter("idBook")));
                    bookDAO.update(book);
                    response.sendRedirect(request.getContextPath() + "/books");
                }
                case "delete" -> {
                    long id = Long.parseLong(request.getParameter("idBook"));
                    bookDAO.delete(id);
                    response.sendRedirect(request.getContextPath() + "/books");
                }
                default -> response.sendRedirect(request.getContextPath() + "/books");
            }
        } catch (SQLException e) {
            throw new ServletException("Error al acceder a la base de datos", e);
        }
    }

    private void showList(HttpServletRequest request, HttpServletResponse response, Book modalBook, String formTitle)
            throws SQLException, ServletException, IOException {
        String search = request.getParameter("search");
        int page = parsePositiveInt(request.getParameter("page"), 1);

        List<Book> allBooks = bookDAO.findAll(search);
        int totalPages = Math.max(1, (int) Math.ceil(allBooks.size() / (double) PAGE_SIZE));
        page = Math.min(Math.max(page, 1), totalPages);

        int fromIndex = Math.min((page - 1) * PAGE_SIZE, allBooks.size());
        int toIndex = Math.min(fromIndex + PAGE_SIZE, allBooks.size());
        List<Book> pageBooks = new ArrayList<>(allBooks.subList(fromIndex, toIndex));

        request.setAttribute("books", pageBooks);
        request.setAttribute("search", search == null ? "" : search);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalBooks", allBooks.size());
        request.setAttribute("modalBook", modalBook);
        request.setAttribute("formTitle", formTitle);
        request.getRequestDispatcher("/WEB-INF/views/books.jsp").forward(request, response);
    }

    private Book readBookFromRequest(HttpServletRequest request) {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String category = request.getParameter("category");
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        return new Book(title, author, category, price, stock);
    }

    private int parsePositiveInt(String value, int defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Math.max(1, Integer.parseInt(value));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
