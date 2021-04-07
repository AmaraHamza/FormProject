package com.proEx.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Restriction implements Filter {

    public static final String ACCES_PUBLIC     = "/PublicPage.jsp";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";

    public void init( FilterConfig config ) throws ServletException {
    }

    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain ) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession();

        if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
            // Redirect Public Page
            response.sendRedirect( request.getContextPath() + ACCES_PUBLIC );
            ;
        } else {
            // Display Member Page
            chain.doFilter( request, response );
        }
    }

    public void destroy() {
    }

}
