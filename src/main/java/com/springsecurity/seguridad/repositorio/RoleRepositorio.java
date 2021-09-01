package com.springsecurity.seguridad.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springsecurity.seguridad.domain.Role;

public interface RoleRepositorio extends JpaRepository<Role, Long> {

	Role findByName(String name);
	
}
