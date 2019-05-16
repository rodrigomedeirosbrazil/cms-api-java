package br.com.medeirostec.cms.services;

import java.util.Optional;

import br.com.medeirostec.cms.entities.Usuario;

public interface UsuarioService {
	
	/**
	 * Persiste um usuário na base de dados.
	 * 
	 * @param usuario
	 * @return Usuario
	 */
	Usuario persistir(Usuario usuario);
	/**
	 * Busca e retorna um usuário dado um email.
	 * 
	 * @param email
	 * @return Optional<Usuario>
	 */
	Optional<Usuario> buscarPorEmail(String email);
	
	/**
	 * Busca e retorna um usuário por ID.
	 * 
	 * @param id
	 * @return Optional<Usuario>
	 */
	Optional<Usuario> buscarPorId(Long id);

}