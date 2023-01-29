<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Order overview">
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
        <tags:orderOverviewRow name="firstName" order="${order}" label="First Name"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="lastName" order="${order}" label="Last Name"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="phone" order="${order}" label="Phone"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="deliveryDate" order="${order}" label="Delivery date"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="deliveryAddress" order="${order}" label="Delivery address"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="paymentMethod" order="${order}" label="Payment method"></tags:orderOverviewRow>
    </table>
</tags:master>