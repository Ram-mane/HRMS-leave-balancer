package com.hrms.app.Security;

//import com.hrms.app.service.CustomUserDetailsServices;
//import com.hrms.app.service.CustomOrganizationDetailsServices;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

//    @Autowired
//    CustomUserDetailsServices customUserDetailsServices;

//    @Autowired
//    CustomOrganizationDetailsServices customOrganizationDetailsServices;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    UserDetails userDetails;

    @Autowired
    private  JwtTokenHelper jwtTokenHelper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {
        // get token

        String requestToken = request.getHeader("Authorization");

        // requesttoken = Bearer+token(Bearer 00126644fsdfjjh)

        System.out.println("requestToken :"+requestToken);

        String username  = null;
        String token = null;

        if(requestToken !=null && requestToken.startsWith("Bearer")) {

            token = requestToken.substring(7);
            System.out.println(token);
//			System.out.println("Decoded Claims: " + jwtTokenHelper.getAllClaimsFromToken(token));
            try {
                username = this.jwtTokenHelper.getUsernameFromToken(token);
                System.out.println("username-----"+username);
            }
            catch(IllegalArgumentException e) {
                System.out.println("Unable to get Token");

            }
            catch(ExpiredJwtException e) {
                System.out.println("Token Expired");

            }
            catch(MalformedJwtException e) {
                System.out.println("Invalid jwt ");

            }

        }else {
            System.out.println("jwt token does not start with Bearer ");
        }


        // once we have set token then validate it

        if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            System.out.println("authorities---------"+userDetails.getAuthorities());

            if(this.jwtTokenHelper.validateToken(token, userDetails)) {

                // if all going ok then authenticate
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }else {
                System.out.println("Invalid Token");
            }
        }else {
            System.out.println("username is null or context is not null ");
        }

        filterChain.doFilter(request, response);
    }




}
