package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.CartServiceImpl;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MiniCartServletTest {
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
    private static final String MINICART_JSP = "/WEB-INF/pages/minicart.jsp";
    private MiniCartServlet servlet = new MiniCartServlet();

    @Before
    public void setup() throws ServletException, OutOfStockException {
        servlet.init(config);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);

    }
    @Test
    public void testService() throws ServletException, IOException {

        servlet.service(request, response);

        verify(request.getRequestDispatcher(MINICART_JSP)).include(request, response);

    }

}
