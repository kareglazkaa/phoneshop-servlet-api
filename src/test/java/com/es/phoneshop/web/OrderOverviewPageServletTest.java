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
    private HttpSession session;
    @Mock
    private RequestDispatcher requestDispatcher;
    private DemoDataServletContextListener demo = new DemoDataServletContextListener();
    private OrderOverviewPageServlet servlet = new OrderOverviewPageServlet();

    @Before
    public void setup() throws ServletException, OutOfStockException {
        servlet.init(config);

        demo.setSampleProducts();

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);

        when(request.getPathInfo()).thenReturn("/" + TestHelper.createOrder().getSecureId());
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
    }
}
