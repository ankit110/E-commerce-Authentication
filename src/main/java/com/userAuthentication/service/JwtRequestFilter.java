package com.userAuthentication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userAuthentication.exception.CustomErrorResponse;
import com.userAuthentication.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    CustomErrorResponse customErrorResponse;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                setCustomErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "Token has expired", response);
                return;
            } catch (MalformedJwtException e) {
                setCustomErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Invalid token format", response);
                return;
            } catch (SignatureException e) {
                logger.debug("SignatureException: Invalid token signature");
                setCustomErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "Invalid token signature", response);
                return;
            } catch (UnsupportedJwtException e) {
                setCustomErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Token is unsupported", response);

                return;
            } catch (IllegalArgumentException e) {
                setCustomErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Token claims string is empty", response);
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

    private void setCustomErrorResponse(int status, String error, String message, HttpServletResponse response) throws IOException {
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(status, error, message);
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = new ObjectMapper().writeValueAsString(customErrorResponse);
        response.getWriter().write(jsonResponse);
    }
}
