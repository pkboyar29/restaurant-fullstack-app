package com.example.backend.security;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTCore jwtCore;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public JWTFilter(JWTCore jwtCore, UserDetailsServiceImpl userDetailsService) {
        this.jwtCore = jwtCore;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        String username = null;
        UserDetails userDetails;
        UsernamePasswordAuthenticationToken auth;

        try {
            String headerAuth = request.getHeader("Authorization");
            if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
                jwt = headerAuth.substring(7);
                System.out.println("current jwt: " + jwt);
            }
            if (jwt != null) {
                try {
                    username = jwtCore.getUsernameFromJwt(jwt);
                    System.out.println(username);
                } catch (ExpiredJwtException e) {
                    System.out.println("Token is expired");
                } catch (MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
                    // i receive SignatureException when manually change my payload
                    System.out.println("token parse error");
                    System.out.println(e);
                }

                // если пользователь не аутентифицирован (то есть нету объекта Authentication), то мы аутентифицируем его на основе данных из jwt (такое может быть например после перезапуска сервера)
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    System.out.println("trigger");
                    userDetails = userDetailsService.loadUserByUsername(username);
                    auth = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    System.out.println("получившийся auth в jwtfilter = " + auth);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception e) {
            // TODO
        }
        filterChain.doFilter(request, response); // passing handling request to next filter in filter chain
    }
}
