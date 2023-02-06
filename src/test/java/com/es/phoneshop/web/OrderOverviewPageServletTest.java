package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.CartServiceImpl;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.model.order.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderOverviewPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletConfig config;
    @Mock
    private RequestDispatcher requestDispatcher;
    private DemoDataServletContextListener demo = new DemoDataServletContextListener();
    private OrderOverviewPageServlet servlet = new OrderOverviewPageServlet();
    private CartService cartService= CartServiceImpl.getInstance();
    private OrderService orderService= OrderServiceImpl.getInstance();
    private String[] productIds = new String[]{"0", "1", "2"};
    private String[] quantities = new String[]{"1", "3", "2"};

    @Before
    public void setup() throws ServletException, OutOfStockException {
        servlet.init(config);

        demo.setSampleProducts();

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        Cart cart = new Cart();

        for (int i = 0; i < 3; i++) {
            cartService.add(cart, Long.valueOf(productIds[i]),
                    Integer.valueOf(quantities[i]));
        }

        Order order=orderService.getOrder(cart);
        orderService.placeOrder(order);

        when(request.getPathInfo()).thenReturn("/" + order.getSecureId());
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
    }
}