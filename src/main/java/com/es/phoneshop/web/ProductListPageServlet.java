package com.es.phoneshop.web;

import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.model.searchHistory.SearchHistoryService;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao = ArrayListProductDao.getINSTANCE();
    private SearchHistoryService searchHistoryService = SearchHistoryService.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortFiled = request.getParameter("sort");
        String sortOrder = request.getParameter("order");

        request.setAttribute("searchHistory", searchHistoryService.getProducts(request));
        request.setAttribute("products",
                productDao.findProducts(
                        Optional.ofNullable(query).map(String::valueOf).orElse(""),
                        Optional.ofNullable(sortFiled).map(SortField::valueOf).orElse(null),
                        Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)));

        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);

    }

}
