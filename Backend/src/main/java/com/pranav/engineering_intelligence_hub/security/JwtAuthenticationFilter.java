package com.pranav.engineering_intelligence_hub.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
        throws ServletException, IOException {
        Cookie[] cookies=req.getCookies();
        if(cookies==null){
            filterChain.doFilter(req, res);
            return;
        }
        String token=null;
        for(Cookie cookie:cookies){
            if("jwt".equals(cookie.getName())){
                token=cookie.getValue();
                break;
            }
        }
        if(token==null){
            filterChain.doFilter(req, res);
            return;
        }
        try{
            String username=jwtService.extractUsername(token);
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails=customUserDetailsService.loadUserByUsername(username);
                if(jwtService.isTokenValid(token,userDetails)){
                    UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(req)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch(JwtException |UsernameNotFoundException e){
            //ignore
        }
        filterChain.doFilter(req, res);
    }
}
