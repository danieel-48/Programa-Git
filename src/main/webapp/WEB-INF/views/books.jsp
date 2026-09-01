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
        <form method="get" action="${pageContext.request.contextPath}/books">
            <label style="font-size:14px;color:#334155;display:grid;gap:6px;">
                Search by ID or title
                <input class="search-input" type="text" name="search" value="${search}"
                       placeholder="Example: 12 or Don Quixote">
            </label>
        </form>
    </div>

    <div class="card" style="padding:0;overflow:hidden;">
        <table>
            <thead>
            <tr>
                <th>Book</th>
                <th>Category</th>
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
                    <td><fmt:formatNumber value="${book.price}" type="currency" currencySymbol="$"/></td>
                    <td>${book.stock}</td>
                    <td>
                        <div class="actions">
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
                    <td colspan="5" style="text-align:center;color:#64748b;padding:24px;">No books found.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>

    <div class="card pagination">
        <p>Showing ${books.size()} of ${totalBooks} books (page ${currentPage} of ${totalPages})</p>
        <div class="actions">
            <a class="btn btn-secondary"
               href="${pageContext.request.contextPath}/books?search=${search}&page=${currentPage - 1}">Previous</a>
            <a class="btn btn-primary"
               href="${pageContext.request.contextPath}/books?search=${search}&page=${currentPage + 1}">Next</a>
        </div>
    </div>

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
                        <input type="text" name="author" value="${modalBook.author}">
                    </label>
                    <label>
                        Category
                        <input type="text" name="category" value="${modalBook.category}">
                    </label>
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
