package com.gc.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

	private String privateKey = "jsdhvihabsiuhaicnownviudshivunoiujdoiva";
	private SecretKey key;

	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder().addClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 60 * 60)).signWith(getKey()).compact();
	}

	private Key getKey() {
		key = Keys.hmacShaKeyFor(privateKey.getBytes());
		return key;
	}

	public String extractUsername(String token) {
		return extractClaims(token).getSubject();
	}

	public Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public boolean validateToken(String username, UserDetails userdetails, String token) {
		return username.equals(userdetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractClaims(token).getExpiration().before(new Date());
	}
}
