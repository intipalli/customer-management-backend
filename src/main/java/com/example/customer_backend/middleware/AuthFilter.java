package com.example.customer_backend.middleware;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.customer_backend.utils.JWTFactory;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class AuthFilter implements Filter{

	private String auth_scope = "com.example.auth.apis";
	private String api_scope = "com.example.data.apis";
	
    @Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String uri = req.getRequestURI();
		
		String tokenheader = req.getHeader("tokencheck");
		if( tokenheader != null && !tokenheader.equalsIgnoreCase("true") ) {
			chain.doFilter(request, response);
			return;		
		}
		
		// auth checking will not apply to these cases
		if(   !uri.startsWith("/api/events") 
	       && !uri.startsWith("/api/registrations")
	       && !uri.equals("/api/customers")
	       ) {
			chain.doFilter(request, response);
			return;			
		}else{
			// check JWT token
			String authheader = req.getHeader("authorization");
			if(authheader != null && authheader.length() > 7 && authheader.startsWith("Bearer")) {
				String jwt_token = authheader.substring(7, authheader.length());
				if(JWTFactory.verifyToken(jwt_token)) {
					String request_scopes = JWTFactory.getScopes(jwt_token);
					if(request_scopes.contains(api_scope) || request_scopes.contains(auth_scope)) {
						chain.doFilter(request, response);
						return;
					}
				}
			}
		}		
		res.sendError(HttpServletResponse.SC_FORBIDDEN, "failed authentication");

	}
}
