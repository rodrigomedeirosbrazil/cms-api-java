package br.com.medeirostec.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.medeirostec.cms.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	@Transactional(readOnly = true)
	Usuario findByEmail(String email);
}