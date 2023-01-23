package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.CartServiceImpl;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.error.ErrorHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CartPageServlet", value = "/CartPageServlet")
public class CartPageServlet extends HttpServlet {
    private CartService cartService = new CartServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartService.getCart(request.getSession()));
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
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
                quantity = getQuantity(quantities[i], request);
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

    private int getQuantity(String quantity, HttpServletRequest request) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(request.getLocale());
        return numberFormat.parse(quantity).intValue();
    }

}
