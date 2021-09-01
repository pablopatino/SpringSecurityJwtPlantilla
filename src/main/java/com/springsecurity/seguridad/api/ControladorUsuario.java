package com.springsecurity.seguridad.api;

import java.net.URI;
import java.util.List;

import javax.servlet.Servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.springsecurity.seguridad.domain.Role;
import com.springsecurity.seguridad.domain.RoleToUsuarioForm;
import com.springsecurity.seguridad.domain.Usuario;
import com.springsecurity.seguridad.services.UsuarioServices;


@RestController
@RequestMapping("/api")
public class ControladorUsuario {

	private final UsuarioServices usuarioServices;

	@Autowired
	public ControladorUsuario(UsuarioServices usuarioServices) {
		this.usuarioServices = usuarioServices;
	}

	@GetMapping("/usuarios")
	public ResponseEntity<List<Usuario>> getUsuarios() {
		return ResponseEntity.ok().body(this.usuarioServices.getAllUsuarios());
	}
	
	@PostMapping("/usuario/guardar")
	public ResponseEntity<Usuario> saveUsuario(@RequestBody Usuario usuario) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/usuario/guardar").toUriString());
		return ResponseEntity.created(uri).body(this.usuarioServices.guardarUsuario(usuario));
	}
	
	@PostMapping("/role/guardar")
	public ResponseEntity<Role> saveRole(@RequestBody Role role) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/guardar").toUriString());
		return ResponseEntity.created(uri).body(this.usuarioServices.guardarRole(role));
	}
	
	@PostMapping("/role/agregarausuario")
	public ResponseEntity<?> agregarRoleAUsuario(@RequestBody RoleToUsuarioForm roleToUsuarioForm) {
		this.usuarioServices.agregarRoleAlUsuario(roleToUsuarioForm.getNombreUsuario(), roleToUsuarioForm.getNombreRole());
		return ResponseEntity.ok().build();
	}

}
