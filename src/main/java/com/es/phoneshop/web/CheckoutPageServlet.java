package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.CartServiceImpl;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.model.order.OrderServiceImpl;
import com.es.phoneshop.enums.PaymentMethod;

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
    private Order order=new Order();

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
            order = orderService.getOrder(cart);

            Map<String, String> errors = new HashMap<>();

            setRequiredParameter(request, "firstName", errors, order::setFirstName);
            setRequiredParameter(request, "lastName", errors, order::setLastName);
            setRequiredParameter(request, "phone", errors, order::setPhone);
            setRequiredParameter(request, "deliveryDate", errors, this::parseDeliverDate);
            setRequiredParameter(request, "deliveryAddress", errors, order::setDeliveryAddress);
            setRequiredParameter(request, "paymentMethod", errors, this::parsePaymentMethod);

            handleErrors(request, response, errors);
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

    private void parseDeliverDate(String deliverDateString) {
        LocalDate deliveryDate = LocalDate.parse(deliverDateString);
        order.setDeliveryDate(deliveryDate);
    }

    private void parsePaymentMethod(String paymentMethodString) {
        PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentMethodString);
        order.setPaymentMethod(paymentMethod);
    }

    private void handleErrors(HttpServletRequest request, HttpServletResponse response,
                              Map<String, String> errors) throws IOException, ServletException {
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
