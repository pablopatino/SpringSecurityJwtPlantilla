package com.springsecurity.seguridad.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springsecurity.seguridad.domain.Role;
import com.springsecurity.seguridad.domain.Usuario;
import com.springsecurity.seguridad.repositorio.RoleRepositorio;
import com.springsecurity.seguridad.repositorio.UsuarioRepositorio;

import lombok.RequiredArgsConstructor;


//UserDetailsServices sirve para que springSecurity sepa de donde agarrar los usuarios

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServicesImlp implements UsuarioServices, UserDetailsService {

	private final UsuarioRepositorio usuarioRepositorio;
	private final RoleRepositorio roleRepositorio;
	private final PasswordEncoder passwordEncoder;
	

	public UsuarioServicesImlp(UsuarioRepositorio usuarioRepositorio, RoleRepositorio roleRepositorio,
			PasswordEncoder passwordEncoder) {
		this.usuarioRepositorio = usuarioRepositorio;
		this.roleRepositorio = roleRepositorio;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Usuario guardarUsuario(Usuario usuario) {
		
		//Para encryptar un password
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		
		return this.usuarioRepositorio.save(usuario);
	}

	@Override
	public Role guardarRole(Role role) {
		return this.roleRepositorio.save(role);
	}

	@Override
	public void agregarRoleAlUsuario(String username, String roleName) {
		Usuario usuario = this.usuarioRepositorio.findByUsername(username);
		Role role = this.roleRepositorio.findByName(roleName);

		usuario.getRoles().add(role);

	}

	@Override
	public Usuario getUsuario(String username) {
		return this.usuarioRepositorio.findByUsername(username);
	}

	@Override
	public List<Usuario> getAllUsuarios() {
		 return (List<Usuario>) this.usuarioRepositorio.findAll();
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = this.usuarioRepositorio.findByUsername(username);
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuario no encontrado");
		} else {
			
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		usuario.getRoles().forEach(role ->{
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		
		return new User(usuario.getUserName(), usuario.getPassword(), authorities);
	}

}
