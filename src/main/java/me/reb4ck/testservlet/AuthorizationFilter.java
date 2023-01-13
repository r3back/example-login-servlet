package me.reb4ck.testservlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@WebFilter("/testservlet")
public class AuthorizationFilter implements Filter {

    private Map<String, String> passwords;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        passwords = new ConcurrentHashMap<>();

        passwords.put("admin", "admin");
        passwords.put("developer", "123");
    }

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
