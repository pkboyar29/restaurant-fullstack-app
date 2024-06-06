package com.example.backend.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private JWTCore jwtCore;
    private UserDetailsServiceImpl UserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        String username = null;
        UserDetails userDetails = null;
        UsernamePasswordAuthenticationToken auth = null;

        try {
            String headerAuth = request.getHeader("Authorization");
            if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
                jwt = headerAuth.substring(7);
                System.out.println("текущий jwt: " + jwt);
            }
            if (jwt != null) {
                try {
                    username = jwtCore.getSubjectFromJwt(jwt);
                } catch (ExpiredJwtException e) {
                    System.out.println("Token is expired");
                } catch (MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
                    System.out.println("token parse error");
                    System.out.println(e);
                }

                // если пользователь не аутентифицирован (то есть нету объекта Authentication), то мы аутентифицируем его на основе данных из jwt (такое может быть например после перезапуска сервера)
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    userDetails = UserDetailsService.loadUserByUsername(username);
                    auth = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

        } catch (Exception e) {
            // TODO
        }

        filterChain.doFilter(request, response); // passing handling request to next filter in filter chain
    }
}
