package com.es.phoneshop.model.order;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.web.DemoDataServletContextListener;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ArrayListOrderDaoTest {
    private OrderDao orderDao = ArrayListOrderDao.getInstance();
    private DemoDataServletContextListener demo = new DemoDataServletContextListener();
    private Order order;

    @Before
    public void setup() throws OutOfStockException {
        demo.setSampleProducts();
        order = new Order();
    }

    @Test
    public void testSaveOrder() {
        orderDao.save(order);

        assertNotNull(orderDao.getOrder(order.getId()));
    }

    @Test
    public void testGetOrder() {
        assertNotNull(orderDao.getOrder(order.getId()));
    }

    @Test
    public void testGetOrderBySecureId() {
        assertNotNull(orderDao.getOrderBySecureId(order.getSecureId()));
    }

    @Test(expected = OrderNotFoundException.class)
    public void testDeleteOrder() {
        orderDao.delete(order.getId());

        orderDao.getOrder(order.getId());
    }
}
