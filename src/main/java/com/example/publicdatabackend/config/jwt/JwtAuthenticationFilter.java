package com.example.publicdatabackend.config.jwt;

import com.example.publicdatabackend.config.security.UserPrincipal;
import com.example.publicdatabackend.config.security.CustomUserDetailsService;
import com.example.publicdatabackend.config.redis.RedisTokenStoreService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisTokenStoreService tokenStoreService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (!requestURI.equals("/login") && !requestURI.equals("/register") &&
//                 !requestURI.contains("/api") &&
            !requestURI.startsWith("/swagger-ui") && !requestURI.startsWith("/v3/api-docs") // --> 개발을 위해 임시로 주석처리
        ) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            log.info("No Authorization header or wrong format");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Missing or invalid Authorization header.");
            return;
        }

        String authToken = header.substring(7);
        try {
            if (!jwtTokenProvider.validateToken(authToken)) {
                log.info("Invalid or expired JWT token");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Invalid or expired JWT token.");
                return;
            }

            String username = jwtTokenProvider.getUserIdFromJWT(authToken).toString();
            if (username != null && tokenStoreService.retrieveUserName(authToken) != null) {
                UserPrincipal userDetails = (UserPrincipal) customUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Optionally store or update the token in Redis
                tokenStoreService.storeToken(authToken, username, true);
            } else {
                log.info("Token validation passed but no corresponding user found in Redis or token is not linked to any user.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: No corresponding user found.");
                return;
            }
        } catch (Exception e) {
            log.error("Could not set user authentication in security context", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Error processing the token.");
            return;
        }
    }

        filterChain.doFilter(request,response);
}
}
