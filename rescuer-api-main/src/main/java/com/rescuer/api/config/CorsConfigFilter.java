package com.rescuer.api.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfigFilter implements Filter {
    /**
     * CorsConfigFilter constructor .
     */
    public CorsConfigFilter() {
    }

    /**
     * Overriding doFilter  for enabling the cors request .
     * Headers are Content-Type,Authorization,location,role and activityLog.
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,PUT,PATCH, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Expose-Headers", "Authorization,Link,X-Total-Count,user_type");
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    /**
     * Overiding init  for unabling the cors request .
     */
    @Override
    public void init(FilterConfig filterConfig) {
    }

    /**
     * Overiding destroy  for destroying the cors request .
     */
    @Override
    public void destroy() {
    }
}