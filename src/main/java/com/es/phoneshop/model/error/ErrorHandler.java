package com.es.phoneshop.model.error;

import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Map;

public class ErrorHandler {
    public static void setErrorAttribute(HttpServletRequest request, Exception ex) {
        if (ex.getClass().equals(ParseException.class)) {
            request.setAttribute("error", "Not a number");
        } else {
            OutOfStockException outEx = (OutOfStockException) ex;
            if (outEx.getStockRequested() <= 0) {
                request.setAttribute("error", "Can't be negative or zero");
            } else {
                request.setAttribute("error", "Out of stock, max available " + outEx.getStockAvailable());
            }
        }
    }

    public static void setErrorAttribute(Map<Long, String> errors, Long productId, Exception ex) {
        if (ex.getClass().equals(ParseException.class)) {
            errors.put(productId, "Not a number");
        } else {
            OutOfStockException outEx = (OutOfStockException) ex;
            if (outEx.getStockRequested() <= 0) {
                errors.put(productId, "Can't be negative or zero");
            } else {
                errors.put(productId, "Out of stock, available " + outEx.getStockAvailable());
            }
        }
    }
}
