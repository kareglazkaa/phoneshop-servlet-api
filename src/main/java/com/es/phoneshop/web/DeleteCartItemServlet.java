package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.CartServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "DeleteCartItemServlet", value = "/DeleteCartItemServlet")
public class DeleteCartItemServlet extends HttpServlet {
    CartService cartService = new CartServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getPathInfo().substring(1);

        Cart cart = cartService.getCart(request.getSession());
        cartService.delete(cart, Long.valueOf(productId));

        response.sendRedirect(request.getContextPath() + "/cart?message=Cart item removed successful");
    }
}
