package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.ProductNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PriceHistoryPageServlet extends HttpServlet {
    private ProductDao productDao = ArrayListProductDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getPathInfo().substring(1);
        try {
            request.setAttribute("product", productDao.getProduct(Long.valueOf(productId)));
            request.getRequestDispatcher("/WEB-INF/pages/priceHistory.jsp").forward(request, response);
        } catch (ProductNotFoundException | NumberFormatException ex) {
            response.sendError(404, "Product " + productId + " not found");
        }
    }
}
