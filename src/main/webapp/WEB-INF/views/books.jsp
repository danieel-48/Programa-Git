<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Inventory - BookStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css">
</head>
<body>
<div class="container">
    <div class="header">
        <div>
            <h1>Inventory</h1>
            <p>Manage books, stock levels and pricing.</p>
        </div>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/books?action=new">New Book</a>
    </div>

    <div class="card">
        <form method="get" action="${pageContext.request.contextPath}/books" class="form-grid" style="max-width:none;">
            <label style="font-size:14px;color:#334155;display:grid;gap:6px;">
                Search by title, author or genre
                <input class="search-input" type="text" name="search" value="${search}"
                       placeholder="Example: Tolkien, fantasy or The Hobbit">
            </label>
            <div class="row-2">
                <label>
                    Genre
                    <input type="text" name="category" value="${category}" placeholder="Example: Fantasy">
                </label>
                <label>
                    Language
                    <input type="text" name="language" value="${language}" placeholder="Example: English">
                </label>
            </div>
            <div class="row-2">
                <label>
                    Minimum price
                    <input type="number" name="minPrice" min="0" step="0.01" value="${minPrice}">
                </label>
                <label>
                    Maximum price
                    <input type="number" name="maxPrice" min="0" step="0.01" value="${maxPrice}">
                </label>
            </div>
            <div class="actions">
                <button class="btn btn-primary" type="submit">Apply filters</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/books">Clear</a>
            </div>
        </form>
    </div>

    <div class="card" style="padding:0;overflow:hidden;">
        <table>
            <thead>
            <tr>
                <th>Book</th>
                <th>Category</th>
                <th>Language</th>
                <th>Price</th>
                <th>Stock</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="book" items="${books}">
                <tr>
                    <td>
                        <div style="font-weight:600;">${book.title}</div>
                        <div style="color:#64748b;font-size:13px;">${book.author}</div>
                    </td>
                    <td><span class="badge">${book.category}</span></td>
                    <td>${book.language}</td>
                    <td><fmt:formatNumber value="${book.price}" type="currency" currencySymbol="$"/></td>
                    <td>${book.stock}</td>
                    <td>
                        <div class="actions">
                            <a class="icon-btn icon-edit" title="View details"
                               href="${pageContext.request.contextPath}/books?action=detail&id=${book.idBook}">&#128269;</a>
                            <a class="icon-btn icon-edit" title="Edit book"
                               href="${pageContext.request.contextPath}/books?action=edit&id=${book.idBook}">&#9998;</a>
                            <form class="confirm-box" method="post"
                                  action="${pageContext.request.contextPath}/books"
                                  onsubmit="return confirm('Are you sure you want to delete this book?');">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="idBook" value="${book.idBook}">
                                <button class="icon-btn icon-delete" title="Delete book" type="submit">&#128465;</button>
                            </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty books}">
                <tr>
                    <td colspan="6" style="text-align:center;color:#64748b;padding:24px;">No books found.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>

    <div class="card pagination">
        <p>Showing ${books.size()} of ${totalBooks} books (page ${currentPage} of ${totalPages})</p>
        <div class="actions">
            <a class="btn btn-secondary"
               href="${pageContext.request.contextPath}/books?search=${search}&category=${category}&language=${language}&minPrice=${minPrice}&maxPrice=${maxPrice}&page=${currentPage - 1}">Previous</a>
            <a class="btn btn-primary"
               href="${pageContext.request.contextPath}/books?search=${search}&category=${category}&language=${language}&minPrice=${minPrice}&maxPrice=${maxPrice}&page=${currentPage + 1}">Next</a>
        </div>
    </div>

    <c:if test="${not empty detailBook}">
        <div class="modal-overlay">
            <div class="modal-card">
                <div class="modal-header">
                    <h2>Book details</h2>
                    <a class="modal-close" href="${pageContext.request.contextPath}/books" aria-label="Close">&times;</a>
                </div>
                <div class="form-grid">
                    <div><strong>Title</strong><br>${detailBook.title}</div>
                    <div><strong>Author</strong><br>${detailBook.author}</div>
                    <div><strong>Genre</strong><br>${detailBook.category}</div>
                    <div><strong>Language</strong><br>${detailBook.language}</div>
                    <div><strong>Price</strong><br><fmt:formatNumber value="${detailBook.price}" type="currency" currencySymbol="$"/></div>
                    <div><strong>Available stock</strong><br>${detailBook.stock}</div>
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${not empty formTitle}">
        <div class="modal-overlay">
            <div class="modal-card">
                <div class="modal-header">
                    <h2>${formTitle}</h2>
                    <a class="modal-close" href="${pageContext.request.contextPath}/books" aria-label="Close">&times;</a>
                </div>
                <form class="form-grid" method="post" action="${pageContext.request.contextPath}/books">
                    <input type="hidden" name="action" value="${empty modalBook.idBook ? 'create' : 'update'}">
                    <input type="hidden" name="idBook" value="${modalBook.idBook}">

                    <label>
                        Title
                        <input type="text" name="title" value="${modalBook.title}" required>
                    </label>
                    <label>
                        Author
                        <input type="text" name="author" value="${modalBook.author}"
                               required pattern=".*\S.*"
                               title="Author is required and cannot be blank">
                    </label>
                    <label>
                        Category
                        <input type="text" name="category" value="${modalBook.category}"
                               required pattern=".*\S.*"
                               title="Category is required and cannot be blank">
                    </label>
                    <label>
                        Language
                        <input type="text" name="language" value="${modalBook.language}"
                               required pattern="(?=.*\S)[^0-9]*"
                               title="Language is required and cannot contain numbers">
                    </label>
                    <c:if test="${not empty param.error}">
                        <p class="error-text">${param.error}</p>
                    </c:if>
                    <div class="row-2">
                        <label>
                            Price
                            <input type="number" name="price" min="0" step="0.01" value="${modalBook.price}" required>
                        </label>
                        <label>
                            Stock
                            <input type="number" name="stock" min="0"
                                   value="${empty modalBook.idBook ? '' : modalBook.stock}" required>
                        </label>
                    </div>
                    <button class="btn btn-primary btn-modal-submit" type="submit">${empty modalBook.idBook ? 'Save' : 'Update'}</button>
                </form>
            </div>
        </div>
    </c:if>
</div>
</body>
</html>
