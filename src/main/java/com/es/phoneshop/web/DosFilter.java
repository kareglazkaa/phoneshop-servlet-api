package com.es.phoneshop.web;

import com.es.phoneshop.security.DosProtectionService;
import com.es.phoneshop.security.DosProtectionServiceImpl;

import javax.servlet.*;
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
            System.out.println("E");
            filterChain.doFilter(request, response);
        } else {
            System.out.println("N");
            ((HttpServletResponse) response).setStatus(429);
        }
    }

    @Override
    public void destroy() {

    }
}
