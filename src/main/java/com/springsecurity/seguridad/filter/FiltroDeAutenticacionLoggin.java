package com.springsecurity.seguridad.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;



//Se tiene que extender de UserNamePasswordAuthenticationFilter para para sobres escribir el metdo
public class FiltroDeAutenticacionLoggin extends UsernamePasswordAuthenticationFilter {

	public static final String APPLICATION_JSON_VALUE = "application/json";
	// Se injecta, por que se va a necesitar
	private final AuthenticationManager authenticationManager;

	@Autowired
	public FiltroDeAutenticacionLoggin(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		// Importar el UserName Authentication Token
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);

		// llamamos al AuthenticationManager para que valide los datos del usuario
		return authenticationManager.authenticate(authenticationToken);
	}

	// Que se hace cuando se logea bien}
	// Si el usuario loguea con el metodo de arriba, spring va a llamar este metodo
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		// GetPrincipal devuelve un objeto, que es el usuario que logueo
		User userSpringSecurity = (User) authentication.getPrincipal();
		// Se crea un algortih, viene de JWT (Libreria)
		Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
		// Crear el token
		String access_token = JWT.create()
				// Puede ser cualquier string que quiera, pero se recomienda una identificacion
				// del usuario (Correo, Id, Nombre Unico) sirve para identificar al usuario
				.withSubject(userSpringSecurity.getUsername())
				// Cuando expira el token
				.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
				// Se pasa aqui el nombre de la compañia o el author del token, aqui pasamos la
				// URL de la request
				.withIssuer(request.getRequestURL().toString())
				// Aqui se pasa los roles para el usuario "roles" es la key de la lista de roles
				// para que aparescan en el token
				.withClaim("roles", userSpringSecurity.getAuthorities().stream().map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList()))
				.sign(algorithm);

		String refresh_token = JWT.create()
				.withSubject(userSpringSecurity.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
				.withIssuer(request.getRequestURL().toString())
				.sign(algorithm);
		
		//Se envian los JWT por el Header
//		response.setHeader("access_token", access_token);
//		response.setHeader("refresh_token", refresh_token);
		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", access_token);
		tokens.put("refresh_token", refresh_token);
		response.setContentType(APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);
		
	}

}
 