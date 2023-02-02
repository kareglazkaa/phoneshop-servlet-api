package com.es.phoneshop.model.order;

import com.es.phoneshop.dao.GenericDao;
import com.es.phoneshop.dao.OrderDao;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao extends GenericDao<Order> implements OrderDao {
    private static final ArrayListOrderDao INSTANCE = new ArrayListOrderDao();
    private ReadWriteLock lock=new ReentrantReadWriteLock();
    private Lock readLock=lock.readLock();
    private ArrayListOrderDao() {
    }
    @Override
    public Order getOrderBySecureId(String secureId) {
        readLock.lock();
        try {
            List<Order> orders = super.getObjects();
            return orders.stream()
                    .filter(order -> secureId.equals(order.getSecureId()))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException());
        }
        finally {
            readLock.unlock();
        }
    }
    public static ArrayListOrderDao getInstance() {
        return INSTANCE;
    }
}

