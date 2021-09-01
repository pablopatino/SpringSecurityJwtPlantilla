package com.springsecurity.seguridad.domain;

public class RoleToUsuarioForm {

	private String nombreUsuario;
	private String nombreRole;
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public String getNombreRole() {
		return nombreRole;
	}
	public void setNombreRole(String nombreRole) {
		this.nombreRole = nombreRole;
	}
	public RoleToUsuarioForm(String nombreUsuario, String nombreRole) {
		this.nombreUsuario = nombreUsuario;
		this.nombreRole = nombreRole;
	}
	public RoleToUsuarioForm() {
	}
	
	
	
	
}
