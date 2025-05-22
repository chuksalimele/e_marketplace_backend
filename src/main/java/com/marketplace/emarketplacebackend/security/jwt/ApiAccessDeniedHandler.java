// ApiAccessDeniedHandler.java
package com.marketplace.emarketplacebackend.security.jwt; // Package remains the same

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiAccessDeniedHandler implements AccessDeniedHandler { // Renamed class

    private static final Logger logger = LoggerFactory.getLogger(ApiAccessDeniedHandler.class); // Update logger

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logger.error("Access Denied error: {}", accessDeniedException.getMessage());

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Error: Access Denied. You do not have permission to access this resource.");
    }
}