package com.jfilho.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jfilho.todolist.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if(!servletPath.startsWith("/tasks")) filterChain.doFilter(request, response);

        else{
            // TODO Auto-generated method stub
            var authorization = request.getHeader("Authorization");

            if(authorization == null) response.sendError(401);

            else {

                var authEncoded = authorization.substring("Basic".length()).trim();

                byte[] authDecode = Base64.getDecoder().decode(authEncoded);

                var authString = new String(authDecode);

                String[] credentials = authString.split(":");
                String username = credentials.length > 0 ? credentials[0] : null;
                String password = credentials.length > 1 ? credentials[1] : null;

                var user = this.userRepository.findByUsername(username);

                if(user == null || password == null) response.sendError(401);

                else {
                    var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                    if(passwordVerify.verified == false) response.sendError(401);
                    else {
                        request.setAttribute("userId", user.getId());
                        filterChain.doFilter(request, response);
                    }
                }

            }
        }
    }
    
}
