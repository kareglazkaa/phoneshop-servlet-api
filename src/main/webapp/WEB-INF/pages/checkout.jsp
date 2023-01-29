<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Chechout">
    <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
        <br>
        <c:if test="${not empty param.message}">
            <div class="success">
                    ${param.message}
            </div>
        </c:if>
        <c:if test="${not empty errors}">
            <div class="error">
                Error with adding data
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
            </tr>
            </thead>

            <c:forEach var="item" items="${order.items}" varStatus="status">
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
                            ${quantity}
                    </td>
                    <td class="price">
                        <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${item.product.id}"/>
                        <fmt:formatNumber value="${item.product.price}" type="currency"
                                          currencySymbol="${item.product.currency.symbol}"/>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td></td>
                <td></td>
                <td class="price">Subtotal:</td>
                <td class="price">
                    <fmt:formatNumber value="${order.subtotal}" type="currency"
                                      currencySymbol="${cart.items[0].product.currency.symbol}"/></td>
            </tr>

            <tr>
                <td></td>
                <td></td>
                <td class="price">Delivery cost:</td>
                <td class="price">
                    <fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                      currencySymbol="${cart.items[0].product.currency.symbol}"/></td>
            </tr>

            <tr>
                <td></td>
                <td></td>
                <td class="price">Total cost:</td>
                <td class="price">
                    <fmt:formatNumber value="${order.totalCost}" type="currency"
                                      currencySymbol="${cart.items[0].product.currency.symbol}"/></td>
            </tr>
        </table>

        <h2>Yours details</h2>
        <table>
            <tags:orderFormRow name="firstName" order="${order}" label="First Name"
                               errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="lastName" order="${order}" label="Last Name"
                               errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="phone" order="${order}" label="Phone" errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="deliveryDate" order="${order}" label="Delivery date"
                               errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="deliveryAddress" order="${order}" label="Delivery address"
                               errors="${errors}"></tags:orderFormRow>
            <tr>
                <td>Payment method</td>
                <td>
                    <select name="paymentMethod">
                        <option></option>
                        <c:forEach var="paymentMethod" items="${paymentMethods}">
                            <c:choose>
                                <c:when test="${paymentMethod eq order.paymentMethod}">
                                    <selected>${paymentMethod}</selected>
                                </c:when>
                                <c:otherwise>
                                    <option>${paymentMethod}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${not empty order.paymentMethod}">
                            <option selected>${order.paymentMethod}</option>
                        </c:if>
                    </select>
                    <c:set var="error" value="${errors['paymentMethod']}"></c:set>
                    <c:if test="${not empty error}">
                        <div class="error">
                                ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <p>
            <button>Place order</button>
        </p>
    </form>
</tags:master>