<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="searchHistory" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <form>
        <p>
            Welcome to Expert-Soft training!
        </p>

        <c:if test="${not empty param.message}">
            <div class="success">
                    ${param.message}
            </div>
        </c:if>

        <c:if test="${not empty errors}">
            <div class="error">
                There were errors add to cart
            </div>
        </c:if>

        <input name="query" value="${param.query}">
        <button>Search</button>

        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                    <tags:sortLink sort="DESCRIPTION" order="ASC"></tags:sortLink>
                    <tags:sortLink sort="DESCRIPTION" order="DESC"></tags:sortLink>
                </td>
                <td>Quantity</td>
                <td class="price">
                    Price
                    <tags:sortLink sort="PRICE" order="ASC"></tags:sortLink>
                    <tags:sortLink sort="PRICE" order="DESC"></tags:sortLink>
                </td>
                <td></td>
            </tr>
            </thead>
            <c:forEach var="product" items="${products}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile" src="${product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${product.id}"/>
                            ${product.description}
                    </td>

                    <td>
                        <c:set var="error" value="${errors[product.id] }"/>
                        <input form="addCartItem"
                               name="quantity" value="1" class="quantity">
                        <c:if test="${not empty error}">
                            <div class="error">
                                    ${error}
                            </div>
                        </c:if>
                    </td>

                    <td class="price">
                        <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${product.id}"/>
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </td>

                    <td>
                        <button form="addCartItem"
                                formaction="${pageContext.servletContext.contextPath}/products?&id=${product.id}&index=${status.index}">
                            Add to cart
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <table>
            <h3>Recently viewed</h3>
            <c:forEach var="product" items="${searchHistory}">
                <td>
                    <p class="info">
                        <img class="product-tile" src="${product.imageUrl}">
                    </p>
                    <p class="info">
                        <a href="${pageContext.servletContext.contextPath}/products/${product.id}"/>
                            ${product.description}
                        <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${product.id}"/>
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </p>
                </td>
            </c:forEach>
        </table>
    </form>
    <form id="addCartItem" method="post"/>
</tags:master>