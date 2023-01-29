package com.es.phoneshop.web;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.order.ArrayListOrderDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {
    private static final String ORDER_OVERVIEW_JSP = "/WEB-INF/pages/orderOverview.jsp";
    private OrderDao orderDao = ArrayListOrderDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String secureId = request.getPathInfo().substring(1);

        request.setAttribute("order", orderDao.getOrderBySecureId(secureId));
        request.getRequestDispatcher(ORDER_OVERVIEW_JSP).forward(request, response);
    }
}
