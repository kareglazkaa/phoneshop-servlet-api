package com.es.phoneshop.web;

import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.CartServiceImpl;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.error.ErrorHandler;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.searchHistory.SearchHistoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao = ArrayListProductDao.getInstance();
    private SearchHistoryServiceImpl searchHistoryServiceImpl = SearchHistoryServiceImpl.getInstance();
    private CartService cartService = CartServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortFiled = request.getParameter("sort");
        String sortOrder = request.getParameter("order");

        request.setAttribute("searchHistory", searchHistoryServiceImpl.getSearchHistory(request).getProducts());
        request.setAttribute("products",
                productDao.findProducts(
                        Optional.ofNullable(query).map(String::valueOf).orElse(""),
                        Optional.ofNullable(sortFiled).map(SortField::valueOf).orElse(null),
                        Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)));

        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<Long, String> errors = new HashMap<>();

        Long productId = Long.valueOf(request.getParameter("id"));
        int index = Integer.parseInt(request.getParameter("index"));
        String[] quantities = request.getParameterValues("quantity");

        try {
            int quantity = getQuantity(quantities[index], request);
            cartService.add(cartService.getCart(request.getSession()), productId, quantity);
        } catch (ParseException | OutOfStockException ex) {
            ErrorHandler.setErrorAttribute(errors, productId, ex);
            request.setAttribute("errors", errors);
            doGet(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/products?message=Product added to cart");

    }

    private int getQuantity(String quantity, HttpServletRequest request) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(request.getLocale());
        return numberFormat.parse(quantity).intValue();
    }

}
