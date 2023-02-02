<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
    <form method="post" action="${pageContext.servletContext.contextPath}/cart">
        <c:if test="${not empty param.message}">
            <div class="success">
                    ${param.message}
            </div>
        </c:if>
        <c:if test="${not empty errors}">
            <div class="error">
                There were errors updating cart
            </div>
        </c:if>

        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td>
                    Quantity
                </td>
                <td>
                    Price
                </td>
                <td></td>
            </tr>
            </thead>
            <c:forEach var="item" items="${cart.items}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile" src="${item.product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}"/>
                            ${item.product.description}
                    </td>
                    <td class="quantity">
                        <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                        <c:set var="error" value="${errors[item.product.id] }"/>
                        <input name="quantity" value="${quantity}"
                               class="quantity"/>
                        <c:if test="${not empty error}">
                            <div class="error">
                                    ${error}
                            </div>
                        </c:if>
                        <input type="hidden" name="productId" value="${item.product.id}">
                    </td>
                    <td class="price">
                        <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${item.product.id}"/>
                        <fmt:formatNumber value="${item.product.price}" type="currency"
                                          currencySymbol="${item.product.currency.symbol}"/>
                    </td>
                    <td>
                        <button form="deleteCartItem"
                                formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">
                            Delete
                        </button>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td></td>
                <td></td>
                <td>Total quantity: ${cart.totalQuantity}</td>
                <td class="price">
                    Total cost: <fmt:formatNumber value="${cart.totalCost}" type="currency"
                                                  currencySymbol="${cart.items[0].product.currency.symbol}"/></td>
            </tr>
        </table>

        <c:if test="${not empty cart.items}">
            <br>
            <button>Update</button>
            </form>
            <form>
                <button formaction="${pageContext.servletContext.contextPath}/checkout">Checkout</button>
            </form>
            <form id="deleteCartItem" method="post"></form>
        </c:if>

</tags:master>