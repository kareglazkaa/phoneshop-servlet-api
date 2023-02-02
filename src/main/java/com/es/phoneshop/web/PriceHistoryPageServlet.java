package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PriceHistoryPageServlet extends HttpServlet {
    private static final String PRICE_HISTORY_JSP = "/WEB-INF/pages/priceHistory.jsp";
    private ArrayListProductDao productDao = ArrayListProductDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getPathInfo().substring(1);
        try {
            request.setAttribute("product", productDao.get(Long.valueOf(productId)));
            request.getRequestDispatcher(PRICE_HISTORY_JSP).forward(request, response);
        } catch (ProductNotFoundException | NumberFormatException ex) {
            response.sendError(404, "Product " + productId + " not found");
        }
    }
}
