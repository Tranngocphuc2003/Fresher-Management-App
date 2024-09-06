package com.springboot.spring_boot_project.filter;
import com.springboot.spring_boot_project.config.CustomJwtDecoder;
import com.springboot.spring_boot_project.service.JwtService;
import com.springboot.spring_boot_project.service.UserInfoService;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Lazy
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private CustomJwtDecoder customJwtDecoder;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,@Nonnull HttpServletResponse response,@Nonnull FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request); // Extract token

        if (token != null) {
            Claims claims = customJwtDecoder.decode(token);
            String username = claims.getSubject(); // Extract username from token

            // If the token is valid and no authentication is set in the context
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userInfoService.loadUserByUsername(username);

                // Validate token and set authentication
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
