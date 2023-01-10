package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "PriceHistoryPageServlet", value = "/PriceHistoryPageServlet")
public class PriceHistoryPageServlet extends HttpServlet {
    private ProductDao productDao= ArrayListProductDao.getInstance();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId=request.getPathInfo();
        System.out.println(productId);
        request.setAttribute("product",productDao.getProduct(Long.valueOf(productId.substring(1))));
        request.getRequestDispatcher("/WEB-INF/pages/priceHistory.jsp").forward(request, response);
    }

}
