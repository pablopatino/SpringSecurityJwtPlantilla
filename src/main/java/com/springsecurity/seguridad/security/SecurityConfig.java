package com.springsecurity.seguridad.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springsecurity.seguridad.filter.AuthorizacionFiltro;
import com.springsecurity.seguridad.filter.FiltroDeAutenticacionLoggin;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	//UserDetailsServices viene de springSecurity
	private final UserDetailsService userDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public SecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//El UserDetailsServices, es lo que arreglamos en el UserServicesImlp
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//Quitar las cookies, para que no se trabaja con sesiones
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		//Se autoriza cualquier request
		http.authorizeRequests().anyRequest().permitAll();
		//Se utiliza un filtro para chekear el usuario cuando vaya a loguear
		http.addFilter(new FiltroDeAutenticacionLoggin(authenticationManagerBean()));
		http.addFilterBefore(new AuthorizacionFiltro(), UsernamePasswordAuthenticationFilter.class);
	}
	
	
	
	//Se crear una funcion AuthenticacionManagar, ya que el filtro lo necesita LA FUNCION SE DEBE LLAMAR ASI COMO ESTA
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}

}
