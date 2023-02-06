package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.SearchMethod;
import com.es.phoneshop.model.product.ArrayListProductDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

public class AdvancedSearchPageServlet extends HttpServlet {
    private static final String ADVANCED_SEARCH_JSP = "/WEB-INF/pages/advancedSearch.jsp";
    private ProductDao productDao = ArrayListProductDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String description = request.getParameter("description");
        String minPriceString = request.getParameter("minPrice");
        String maxPriceString = request.getParameter("maxPrice");

        request.setAttribute("minPriceError", null);
        request.setAttribute("maxPriceError", null);

        BigDecimal minPrice;
        BigDecimal maxPrice;

        try {
            minPrice = BigDecimal.valueOf(Long.parseLong(minPriceString));
        } catch (NumberFormatException e) {
            minPrice = null;
            if (minPriceString != null && !minPriceString.isEmpty())
                request.setAttribute("minPriceError", "Not a number");
        }
        try {
            maxPrice = BigDecimal.valueOf(Long.parseLong(maxPriceString));
        } catch (NumberFormatException e) {
            maxPrice = null;
            if (maxPriceString != null && !maxPriceString.isEmpty())
                request.setAttribute("maxPriceError", "Not a number");
        }


        String searchMethodString = request.getParameter("searchMethod");
        SearchMethod searchMethod = null;
        if (searchMethodString != null) {
            searchMethod = SearchMethod.valueOf(searchMethodString);
        }

        request.setAttribute("products",
                productDao.advancedFindProducts(
                        Optional.ofNullable(description).map(String::valueOf).orElse(""),
                        Optional.ofNullable(minPrice).orElse(null),
                        Optional.ofNullable(maxPrice).orElse(null),
                        Optional.ofNullable(searchMethod).orElse(SearchMethod.ALLWORDS)));


        request.getRequestDispatcher(ADVANCED_SEARCH_JSP).forward(request, response);
    }
}
