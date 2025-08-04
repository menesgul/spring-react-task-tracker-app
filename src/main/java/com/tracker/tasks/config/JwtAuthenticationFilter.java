package com.tracker.tasks.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // OncePerRequestFilter: Bu sınıfı extend ederek Spring Security filter zincirine bir filtre ekleriz.
    // Her HTTP request için yalnızca 1 kez çalışır ve JWT token kontrolü burada yapılır.

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // doFilter ile Spring Security'nin
            // filtre zincirinde bir sonraki filtreye geçiş yaparız.
            return;
            // it is called guard clause . Eğer header yoksa veya yanlıs
            // formatta ise JWT doğrulaması yapmadan geçise izin verir.
            // public endpointler ve stateless yapılar icin mantıklı.
        }
        jwt = authHeader.substring(7);

        userEmail = jwtService.extractUsername(jwt);

        if(userEmail != null  && SecurityContextHolder.getContext().getAuthentication() == null) {
            // we need to check username from database in that situation
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            //validate the token , if user is valid, we need to update Security Context ,
            if(jwtService.isTokenValid(jwt, userDetails)) {

                //  Yeni bir Authentication objesi oluştur (kullanıcı, yetkiler)
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,                // authenticated user bilgisi
                        null,                       // credentials (şifre yok çünkü JWT stateless)
                        userDetails.getAuthorities()// kullanıcının rolleri ve yetkileri
                );

                // Request ile ilgili ek detayları Authentication’a bağla (IP adresi, session info vs.)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Authentication objesini SecurityContext'e yerleştir
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        //  zincirdeki bir sonraki filtreye geç,
        filterChain.doFilter(request, response);
    }
}