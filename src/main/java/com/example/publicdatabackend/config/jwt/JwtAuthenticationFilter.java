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

//http 요청에서 jwt를 검증하고 인증을 처리
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
        final String requestURI = request.getRequestURI();

        //두 로직에서는 JWT검증할 필요가 없기때문에 제외하고 실행
        if (requestURI.contains("/login") || requestURI.contains("/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        String authToken = null;
        if (header != null && header.startsWith("Bearer ")) {
            authToken = header.substring(7);
            try {
                String username = String.valueOf(jwtTokenProvider.getUserIdFromJWT(authToken));
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserPrincipal userDetails = (UserPrincipal) customUserDetailsService.loadUserByUsername(username);
                    if (jwtTokenProvider.validateToken(authToken, userDetails.getUsername()) && tokenStoreService.retrieveUserName(authToken) != null) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.info("Authentication successful. Logged in username: {}", username);

                        tokenStoreService.storeToken(authToken, username,true);
                        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
                        response.setHeader("Refresh-Token", refreshToken);

                    } else {
                        log.info("Token is not valid or expired.");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Unauthorized: Authentication token was either missing or invalid.");
                        return;
                    }
                }
            } catch (Exception e) {
                log.error("Could not set user authentication in security context", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Authentication token was either missing or invalid.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
