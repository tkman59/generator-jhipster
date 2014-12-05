package <%=packageName%>.web.filter;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filter is used to put the CSRF tocken generated by Spring in a cookie for use by Angular.
 * </p>
 */
public class CsrfCookieGeneratorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Spring put CSRF token in session attribut _csrf
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");

        // Send a cookie only if the token has changed
        String actualToken = request.getHeader("X-CSRF-TOKEN");
        if((actualToken == null || !actualToken.equals(csrfToken.getToken()))) {
            // Session Cookie Based Approach for CSRF token
            String pCookieName = "CSRF-TOKEN";
            Cookie cookie = new Cookie(pCookieName, csrfToken.getToken());
            cookie.setMaxAge(-1);
            cookie.setHttpOnly(false);
            cookie.setPath("/");

            response.addCookie(cookie);
        }

        filterChain.doFilter(request, response);
    }

}
