package com.springsecurity.seguridad.services;

import java.util.List;

import com.springsecurity.seguridad.domain.Role;
import com.springsecurity.seguridad.domain.Usuario;

public interface UsuarioServices {

	
	Usuario guardarUsuario(Usuario usuario);
	Role guardarRole(Role role);
	void agregarRoleAlUsuario(String username, String roleName);
	Usuario getUsuario(String username);
	List<Usuario> getAllUsuarios();
	
}
