package com.es.phoneshop.web;

import com.es.phoneshop.model.history.SearchHistory;
import com.es.phoneshop.model.history.SearchHistoryServiceImpl;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private HttpSession session;
    @Mock
    private ServletConfig config;

    private static final String SEARCH_HISTORY_SESSION_ATTRIBUTE =
            SearchHistoryServiceImpl.class.getName() + ".products";

    private ProductDetailsPageServlet servlet=new ProductDetailsPageServlet();
    private DemoDataServletContextListener demo=new DemoDataServletContextListener();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);

        demo.setSampleProducts();

        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession().getAttribute(anyString())).thenReturn(new SearchHistory());
        when(request.getPathInfo()).thenReturn("/0");
    }

    @Test
    public void testDoGet() throws ServletException,IOException{
        servlet.doGet(request,response);

        verify(requestDispatcher).forward(request,response);
        verify(request).setAttribute(eq("product"),any());
    }

}
