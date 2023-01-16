package com.es.phoneshop.model.cart;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);

    void addItem(Cart cart, Long productId, int quantity) throws OutOfStockException;

    void increaseCartQuantity(Cart cart, int index, int quantity);
}
