package com.springsecurity.seguridad.repositorio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springsecurity.seguridad.domain.Usuario;

@Repository
public interface UsuarioRepositorio extends CrudRepository<Usuario, Long>{

	Usuario findByUsername(String username);
	
}
