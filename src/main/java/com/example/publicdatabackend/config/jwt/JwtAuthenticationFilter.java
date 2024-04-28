package com.example.publicdatabackend.config.jwt;

import com.example.publicdatabackend.config.security.UserPrincipal;
import com.example.publicdatabackend.config.security.CustomUserDetailsService;
import com.example.publicdatabackend.config.redis.RedisTokenStoreService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//http 요청에서 jwt를 검증하고 인증을 처리
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisTokenStoreService tokenStoreService;

    private final List<RequestMatcher> permitAllRequestMatchers;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String requestURI = request.getRequestURI();

        if (permitAllMatchers(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        //두 로직에서는 JWT검증할 필요가 없기때문에 제외하고 실행
        if (requestURI.contains("/login") || requestURI.contains("/register") || requestURI.contains("/api")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authToken = header.substring(7);
        try {
            if (!jwtTokenProvider.validateToken(authToken)) {
                throw new JwtException("Invalid or expired JWT token");
            }

            String username = String.valueOf(jwtTokenProvider.getUserIdFromJWT(authToken));
            UserPrincipal userDetails = (UserPrincipal) customUserDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            tokenStoreService.storeToken(authToken, username, false);
            if (shouldIssueNewRefreshToken(authToken)) {
                String newRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);
                tokenStoreService.storeToken(newRefreshToken, username, true);
                response.setHeader("Refresh-Token", newRefreshToken);
            }

            log.info("Authentication successful. Logged in username: {}", username);

        } catch (Exception e) {
            log.error("Authentication error: " + e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean permitAllMatchers(HttpServletRequest request) {
        return permitAllRequestMatchers.stream()
                .anyMatch(matcher -> matcher.matches(request));
    }

    private boolean shouldIssueNewRefreshToken(String authToken) {
        return false;
    }
}
