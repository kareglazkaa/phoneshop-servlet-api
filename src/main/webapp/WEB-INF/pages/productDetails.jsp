<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<jsp:useBean id="searchHistory" type="java.util.ArrayList" scope="request"/>

<tags:master pageTitle="Product List">
    <p>
        Cart: ${cart}
    </p>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="error">
             There was an error to adding to cart
        </div>
    </c:if>
    <p>
       ${product.description}
    </p>
    <form method="post">
    <table>
        <thead>
        <tr>
            <td>Image</td>
           <td>
               <img src="${product.imageUrl}">
           </td>
        </tr>
        <tr>
            <td>code</td>
            <td>
                ${product.code}
            </td>
        </tr>
        <tr>
            <td>stock</td>
            <td>
               ${product.stock}
            </td>
        </tr>
        <tr>
            <td>price</td>
            <td class="price">
                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </td>
        </tr>
        <tr>
            <td>quantity</td>
            <td>
                <input name="quantity" value="${ not empty error? param.quantity:1 }" class="quantity" >
            <c:if test="${not empty error}">
                <div class="error">
                        ${error}
                </div>
            </c:if>
        </td>
        </tr>
    </table>
        <button>Add to cart</button>
    </form>

    <table>
        <h3>Recently viewed</h3>
        <c:forEach var="product" items="${searchHistory}">
            <td>
                <p class="info">
                <img class="product-tile" src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${product.imageUrl}">
                </p>
                <p class="info">
                <a href="${pageContext.servletContext.contextPath}/products/${product.id}"/>
                    ${product.description}
                </p>
                <p class="info">
                    <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${product.id}"/>
                    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                </p>
            </td>
        </c:forEach>
    </table>
</tags:master>