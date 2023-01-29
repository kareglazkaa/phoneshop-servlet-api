package com.es.phoneshop.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private FilterConfig config;

    private DosFilter filter = new DosFilter();

    @Before
    public void setup() {
        filter.init(config);

        when(request.getRemoteAddr()).thenReturn("0:0:0:0:0:0:0:1");
    }

    @Test
    public void testDosFilter() throws ServletException, IOException {
        filter.doFilter(request, response, filterChain);
    }
}
