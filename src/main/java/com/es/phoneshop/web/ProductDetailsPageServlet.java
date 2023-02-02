package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.CartServiceImpl;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.error.ErrorHandler;
import com.es.phoneshop.model.helper.QuantityHelper;
import com.es.phoneshop.model.history.SearchHistoryServiceImpl;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private static final String PRODUCT_DETAILS_JSP = "/WEB-INF/pages/productDetails.jsp";
    private ArrayListProductDao productDao = ArrayListProductDao.getInstance();
    private CartService cartService = CartServiceImpl.getInstance();
    private SearchHistoryServiceImpl searchHistoryServiceImpl = SearchHistoryServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);
        Product product = productDao.get(productId);

        if (product == null) {
            response.sendError(404, "Product " + productId + " not found");
        } else {
            searchHistoryServiceImpl.addRecentProduct(
                    searchHistoryServiceImpl.getSearchHistory(request), product);

            request.setAttribute("product", productDao.get(productId));
            request.setAttribute("searchHistory", searchHistoryServiceImpl.getSearchHistory(request).getProducts());

            request.getRequestDispatcher(PRODUCT_DETAILS_JSP).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantityString = request.getParameter("quantity");
        Long productId = parseProductId(request);
        try {
            int quantity = QuantityHelper.getQuantity(quantityString, request);
            cartService.add(cartService.getCart(request.getSession()), productId, quantity);
        } catch (ParseException | OutOfStockException e) {
            ErrorHandler.setErrorAttribute(request, e);
            doGet(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Product added to cart");
    }

    private Long parseProductId(HttpServletRequest request) {
        String productInfo = request.getPathInfo().substring(1);
        return Long.valueOf(productInfo);
    }


}
