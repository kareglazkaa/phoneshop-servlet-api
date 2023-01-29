package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.CartServiceImpl;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.helper.QuantityHelper;
import com.es.phoneshop.model.error.ErrorHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private static final String CART_JSP = "/WEB-INF/pages/cart.jsp";
    private CartService cartService = CartServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartService.getCart(request.getSession()));
        request.getRequestDispatcher(CART_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productsId = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        Map<Long, String> errors = new HashMap<>();
        int len = productsId.length;
        for (int i = 0; i < len; i++) {
            Long productId = Long.valueOf(productsId[i]);
            int quantity = 0;
            try {
                quantity = QuantityHelper.getQuantity(quantities[i], request);
                cartService.update(cartService.getCart(request.getSession()), productId, quantity);
            } catch (ParseException | OutOfStockException e) {
                ErrorHandler.setErrorAttribute(errors, productId, e);
            }
        }
        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?message=Cart updated successful");
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }

    }
}
