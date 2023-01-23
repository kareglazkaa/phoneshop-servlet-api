package com.es.phoneshop.web;

import com.es.phoneshop.model.searchHistory.SearchHistoryServiceImpl;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.CartServiceImpl;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.ProductNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

@WebServlet(name = "ProductDetailsPageServlet", value = "/ProductDetailsPageServlet")
public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao = ArrayListProductDao.getINSTANCE();
    private CartService cartService = CartServiceImpl.getINSTANCE();
    private SearchHistoryServiceImpl searchHistoryServiceImpl = SearchHistoryServiceImpl.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = productId = parseProductId(request);
        try {
            searchHistoryServiceImpl.addRecentProduct(
                    searchHistoryServiceImpl.getSearchHistory(request), productDao.getProduct(productId));
        } catch (ProductNotFoundException e) {
            response.sendError(404, "Product " + productId + " not found");
        }

        request.setAttribute("product", productDao.getProduct(productId));
        request.setAttribute("cart", cartService.getCart(request));
        request.setAttribute("searchHistory", searchHistoryServiceImpl.getSearchHistory(request).getProducts());

        request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantityString = request.getParameter("quantity");
        Long productId = parseProductId(request);

        int quantity;
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityString).intValue();

        } catch (ParseException e) {
            request.setAttribute("error", "Not a number");
            doGet(request, response);
            return;
        }

        try {
            cartService.addItem(cartService.getCart(request), productId, quantity);
        } catch (OutOfStockException e) {
            request.setAttribute("error", "Out of stock, available " + e.getStockAvailable());
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
