package com.es.phoneshop.model.order;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ArrayListOrderDao implements OrderDao {
    private static final OrderDao INSTANCE = new ArrayListOrderDao();
    private List<Order> orders = new ArrayList<>();
    private AtomicLong maxId = new AtomicLong();

    private ArrayListOrderDao() {
    }

    @Override
    public synchronized Order getOrder(Long id) throws OrderNotFoundException {
        return orders.stream()
                .filter(order -> id.equals(order.getId()))
                .findAny()
                .orElseThrow(() -> new OrderNotFoundException());
    }

    @Override
    public synchronized void save(Order order) {
        Long id = order.getId();
        if (id != null) {
            for (int i = 0; i < orders.size(); i++) {
                if (id.equals(orders.get(i).getId())) {
                    orders.set(i, order);
                    break;
                }
            }
        } else {
            order.setId(maxId.getAndIncrement());
            orders.add(order);
        }

    }

    @Override
    public synchronized Order getOrderBySecureId(String secureId) {
        return orders.stream()
                .filter(order -> secureId.equals(order.getSecureId()))
                .findAny()
                .orElseThrow(() -> new OrderNotFoundException());
    }

    @Override
    public synchronized void delete(Long id) throws OrderNotFoundException {
            Order orderToDelete = orders.stream()
                    .filter(order -> id.equals(order.getId()))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException());

            orders.remove(orderToDelete);

    }

    public synchronized static OrderDao getInstance() {
        return INSTANCE;
    }
}

