package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.ProductNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet(name = "ProductDetailsPageServlet", value = "/ProductDetailsPageServlet")
public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao= ArrayListProductDao.getInstance();
    private CartService cartService= DefaultCartService.getINSTANCE();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = null;
        try {
            productId=parseProductId(request);
            request.setAttribute("product",productDao.getProduct(productId));
            request.setAttribute("cart",cartService.getCart());
            request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
        }
        catch (ProductNotFoundException | NumberFormatException ex){
            response.sendError(404,"Product "+productId+" not found");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantityString=request.getParameter("quantity");
        Long productId=parseProductId(request);
        int quantity;
        try {
            quantity =Integer.valueOf(quantityString);
            cartService.add(productId,quantity);
            request.setAttribute("message","Product added to cart");

        } catch (NumberFormatException e) {
            request.setAttribute("error","Not a number");
        }
        catch (OutOfStockException e){
            request.setAttribute("error","Out of stock, available "+e.getStockAvailable());
        }
        doGet(request,response);
    }

    private Long parseProductId(HttpServletRequest request) {
        String productInfo = request.getPathInfo().substring(1);
        return Long.valueOf(productInfo);
    }
}
