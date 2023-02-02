package com.es.phoneshop.web;

import com.es.phoneshop.security.DosProtectionService;
import com.es.phoneshop.security.DosProtectionServiceImpl;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DosFilter implements Filter {
    private DosProtectionService dosProtectionService;

    @Override
    public void init(FilterConfig filterConfig) {
        dosProtectionService = DosProtectionServiceImpl.getInstance();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws ServletException, IOException {
        if (dosProtectionService.isAllowed(request.getRemoteAddr())) {
            filterChain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).setStatus(429);
        }
    }

    @Override
    public void destroy() {

    }
}
