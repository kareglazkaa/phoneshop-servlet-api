package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.OutOfStockException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DemoDataServletContexListenerTest {
    @Mock
    private ServletContextEvent servletContextEvent;
    @Mock
    private ServletContext servletContext;
    @Mock
    private DemoDataServletContextListener servletContextListener =
            new DemoDataServletContextListener();

    @Before
    public void setup() throws ServletException, OutOfStockException {

        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        when(servletContext.getInitParameter(anyString())).thenReturn("true");
        servletContextListener.contextInitialized(servletContextEvent);
    }

    @Test
    public void testSetSampleProducts() throws ServletException, IOException {

        servletContextListener.setSampleProducts();

        verify(servletContextListener, times(1)).setSampleProducts();
    }
}
