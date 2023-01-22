package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.CartServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "MiniCartServlet", value = "/MiniCartServlet")
public class MiniCartServlet extends HttpServlet {
    private static final String CART_JSP = "/WEB-INF/pages/minicart.jsp";
    private CartService cartService = new CartServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartService.getCart(request.getSession()));

        request.getRequestDispatcher(CART_JSP).include(request, response);
    }

}
