package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.CartServiceImpl;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.model.order.OrderServiceImpl;

public class TestHelper {
    private static CartService cartService = CartServiceImpl.getInstance();
    private static OrderService orderService = OrderServiceImpl.getInstance();
    private static String[] productIds = new String[]{"0", "1", "2"};
    private static String[] quantities = new String[]{"1", "3", "2"};

    public static Cart createCart() throws OutOfStockException {
        Cart cart = new Cart();

        for (int i = 0; i < 3; i++) {
            cartService.add(cart, Long.valueOf(productIds[i]),
                    Integer.valueOf(quantities[i]));
        }
        return cart;
    }

    public static Order createOrder() throws OutOfStockException {
        Order order = orderService.getOrder(createCart());
        orderService.placeOrder(order);
        return order;
    }
}
