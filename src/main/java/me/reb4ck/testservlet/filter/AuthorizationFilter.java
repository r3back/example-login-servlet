package me.reb4ck.testservlet.filter;

import com.google.common.collect.ImmutableMap;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public final class AuthorizationFilter implements Filter {
    private final Map<String, String> passwords = ImmutableMap.<String, String>builder()
            .put("admin", "admin")
            .put("developer", "123")
            .build();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(!validateAuth((HttpServletRequest) servletRequest)){
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access request");
        }else{
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private boolean validateAuth(HttpServletRequest request){
        String login = request.getParameter("login");
        if(login == null || !passwords.containsKey(login)){
            return false;
        }
        String password = request.getParameter("password");

        return password != null && passwords.get(login).equals(password);
    }
}
