package com.edigest.journalApp.filter;

import com.edigest.journalApp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component // ✅ FIX 1: Spring Bean bana diya
public class JwtFilter extends OncePerRequestFilter { // ✅ FIX 2: Spring ka filter extend kar liya

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String authourizationdHeader = req.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authourizationdHeader != null && authourizationdHeader.startsWith("Bearer ")) {
            jwt = authourizationdHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        if (username != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // ✅ FIX 3: .getUsername() hata kar seedha userDetails object pass kiya
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken auth =  new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // ✅ FIX 4: 'response' ko 'res' kar diya kyunki upar parameter 'res' hai
        res.addHeader("admin", "ritesh");
        chain.doFilter(req, res);
    }
}
