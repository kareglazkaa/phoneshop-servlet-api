<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Price history">
    <h1>
       Price history
    </h1>
    <h2>
        ${product.description}
    </h2>
    <p> </p>
    <table border="0">
        <tr>
            <th>Start date</th>
            <th>Price</th>
        </tr>
    <c:forEach var="priceHistory" items="${product.priceHistoryList}">
        <tr>
            <div border:none="0">
            <td>${priceHistory.date}</td>
            <td>${priceHistory.price}</td>
            </div>
        </tr>
    </c:forEach>
    </table>
</tags:master>