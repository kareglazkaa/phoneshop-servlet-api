package com.es.phoneshop.web;

import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.CartServiceImpl;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderNotFoundException;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.model.order.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {
    private static final String CHECKOUT_JSP = "/WEB-INF/pages/checkout.jsp";
    private CartService cartService = CartServiceImpl.getInstance();
    private OrderService orderService = OrderServiceImpl.getInstance();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request.getSession());
        request.setAttribute("order", orderService.getOrder(cart));
        request.setAttribute("paymentMethods", orderService.getPaymentMethods());
        request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request.getSession());
        if(cart.getItems().isEmpty()){
            request.setAttribute("error", "Cart is empty");
            request.setAttribute("order", orderService.getOrder(cart));
            request.setAttribute("paymentMethods", orderService.getPaymentMethods());
            request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
        }
        else {
            Order order = orderService.getOrder(cart);
            if(order==null){
                throw new OrderNotFoundException();
            }

            Map<String, String> errors = new HashMap<>();

            setRequiredParameter(request, "firstName", errors, order::setFirstName);
            setRequiredParameter(request, "lastName", errors, order::setLastName);
            setRequiredParameter(request, "phone", errors, order::setPhone);
            setRequiredDeliveryDate(request, "deliveryDate", errors, order::setDeliveryDate);
            setRequiredParameter(request, "deliveryAddress", errors, order::setDeliveryAddress);
            setRequiredPaymentMethod(request, "paymentMethod", errors,order::setPaymentMethod);

            handleErrors(request, response, errors,order);
        }

    }

    private void setRequiredParameter(HttpServletRequest request, String parameter,
                                      Map<String, String> errors, Consumer<String> consumer) {

        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, "Value is required");
        } else {
            consumer.accept(value);
        }
    }
    private void setRequiredDeliveryDate(HttpServletRequest request, String parameter,
                                          Map<String, String> errors, Consumer<LocalDate> consumer) {

        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, "Value is required");
        } else {
            consumer.accept(LocalDate.parse(value));
        }
    }
    private void setRequiredPaymentMethod(HttpServletRequest request, String parameter,
                                      Map<String, String> errors, Consumer<PaymentMethod> consumer) {

        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, "Value is required");
        } else {
            consumer.accept(PaymentMethod.valueOf(value));
        }
    }


    private void handleErrors(HttpServletRequest request, HttpServletResponse response,
                              Map<String, String> errors, Order order) throws IOException, ServletException {
        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clearCart(cartService.getCart(request.getSession()));
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("order", order);
            request.setAttribute("paymentMethods", orderService.getPaymentMethods());
            request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
        }
    }
}
