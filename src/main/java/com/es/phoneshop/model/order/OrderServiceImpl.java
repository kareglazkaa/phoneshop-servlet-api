package com.es.phoneshop.model.order;

import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private Object lock = new Object();
    private static final OrderServiceImpl INSTANCE = new OrderServiceImpl();
    private ArrayListOrderDao orderDao = ArrayListOrderDao.getInstance();

    private OrderServiceImpl() {
    }

    public static OrderServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public synchronized Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems().stream().map(item -> {
            try {
                return item.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));

        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalCost(order.getSubtotal().add(order.getDeliveryCost()));

        return order;
    }

    @Override
    public synchronized List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public synchronized void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(5);
    }

}
