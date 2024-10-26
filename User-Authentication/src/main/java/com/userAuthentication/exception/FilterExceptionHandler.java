package com.userAuthentication.exception;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FilterExceptionHandler implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (CmsResponseStatusException ex) {
            // Let the global exception handler handle this
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void destroy() {
    }
}
