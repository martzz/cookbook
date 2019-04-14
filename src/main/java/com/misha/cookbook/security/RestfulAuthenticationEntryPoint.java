package com.misha.cookbook.security;

import com.misha.cookbook.configuration.SecurityConfiguration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * REST based entry point. In case of authentication failure, the service should return HTTP 401 and not redirect
 * to any login page (default spring security behavior).
 */
public class RestfulAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    private static final String HTTP_401_PREFIX = "HTTP Status 401 : ";

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");

        PrintWriter writer = response.getWriter();
        writer.println(HTTP_401_PREFIX + authException.getMessage());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName(SecurityConfiguration.REALM_NAME);
        super.afterPropertiesSet();
    }
}
