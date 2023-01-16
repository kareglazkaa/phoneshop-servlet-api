package com.es.phoneshop.model.cart;

import com.es.phoneshop.web.DemoDataServletContextListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    private Cart cart;
    private DefaultCartService cartService = DefaultCartService.getINSTANCE();
    private DemoDataServletContextListener demo = new DemoDataServletContextListener();

    @Before
    public void setup() throws ServletException {

        when(request.getSession()).thenReturn(session);

        demo.setSampleProducts();
        cart = cartService.getCart(request);
    }

    @Test
    public void getCartTest() {
        Cart testCart;
        testCart = cartService.getCart(request);
        assertNotNull(testCart);
    }

    @Test
    public void addItemTest() throws OutOfStockException {
        assertNull(cart.getItems().stream().filter(item -> item.getProduct().getId().equals(0L) && item.getQuantity() == 5).findAny().orElse(null));

        cartService.addItem(cart, 0L, 5);
        assertNotNull(cart.getItems().stream().filter(item -> item.getProduct().getId().equals(0L) && item.getQuantity() == 5).findAny().orElse(null));


        cartService.addItem(cart, 0L, 8);
        assertEquals(cart.getItems().stream().filter(item -> item.getProduct().getId().equals(0L) && item.getQuantity() == 8).count(), 0);
        assertEquals(cart.getItems().stream().filter(item -> item.getProduct().getId().equals(0L) && item.getQuantity() == 13).count(), 1);
    }
}