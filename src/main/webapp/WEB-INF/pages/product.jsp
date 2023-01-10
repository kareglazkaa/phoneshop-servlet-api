<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
       ${product.description}
    </p>
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
            <td>price</td>
            <td>
                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </td>
        </tr>
        <tr>
            <td>stock</td>
            <td>
               ${product.stock}
            </td>
        </tr>
    </table>
</tags:master>