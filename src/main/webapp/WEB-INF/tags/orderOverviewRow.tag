<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order" %>
<%@ attribute name="label" required="true" %>
<tr>
    <td>${label}<span style="color:red">*</span></td>
    <td>
        ${order[name]}
    </td>
</tr>