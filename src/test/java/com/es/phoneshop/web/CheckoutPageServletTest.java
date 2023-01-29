package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.OutOfStockException;
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
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.text.ParseException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletConfig config;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private HttpSession session;
    private DemoDataServletContextListener demo = new DemoDataServletContextListener();

    private CheckoutPageServlet servlet =new CheckoutPageServlet();
    @Before
    public void setup() throws ServletException, OutOfStockException {
        servlet.init(config);

        demo.setSampleProducts();

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(TestHelper.createCart());
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request,response);

        verify(requestDispatcher).forward(request,response);
    }
    @Test
    public void testDoPost() throws ServletException, IOException {
        servlet.doPost(request, response);
    }
}
