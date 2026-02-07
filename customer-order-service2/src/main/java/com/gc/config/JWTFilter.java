package com.gc.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gc.service.CustomerDetailsService;
import com.gc.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

	private CustomerDetailsService customerDetailsService;

	private JWTService jwtService;

	public JWTFilter(CustomerDetailsService customerDetailsService, JWTService jwtService) {
		super();
		this.customerDetailsService = customerDetailsService;
		this.jwtService = jwtService;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();

		return path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui.html")
				|| path.startsWith("/webjars") || path.startsWith("/error") || path.startsWith("/eureka")
				|| path.startsWith("/actuator") ||path.startsWith("/api/customer/login") ||path.startsWith("/api/customer/register");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
		try {
			String authHeader = request.getHeader("Authorization");
			String username = null;
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return; 
			}

			String token = authHeader.substring(7);
			if (token != null && !token.isEmpty()) {
				username = jwtService.extractUsername(token);
			}

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userdetails = customerDetailsService.loadUserByUsername(username);
				if (jwtService.validateToken(username, userdetails, token)) {
					UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(userdetails,
							null, userdetails.getAuthorities());

					authtoken.setDetails(new WebAuthenticationDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authtoken);
				}
			}

			filterChain.doFilter(request, response);
		} catch (IOException | ServletException e) {
			log.error(e.getMessage());
		}

	}

}
