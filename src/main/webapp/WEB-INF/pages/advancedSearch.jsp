<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Advanced search">

    <h2>Advanced search</h2>
    <form>
        <p>
            Description <input name="description">
            <select name="searchMethod">
                <option value="ALLWORDS">all words</option>
                <option value="ANYWORD">any word</option>
            </select>
        </p>
        <p>
            Min price <input name="minPrice">
        <div class="error">
            <c:if test="${not empty minPriceError}">
                ${minPriceError}
            </c:if>
        </div>
        </p>
        <p>
            Max price <input name="maxPrice">
        <div class="error">
            <c:if test="${not empty maxPriceError}">
                ${maxPriceError}
            </c:if>
        </div>

        </p>
        <button>Search</button>
    </form>

    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>Description</td>
            <td>Price</td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}"/>
                        ${product.description}
                </td>
                <td class="price">
                    <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${product.id}"/>
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
        </c:forEach>
    </table>
</tags:master>