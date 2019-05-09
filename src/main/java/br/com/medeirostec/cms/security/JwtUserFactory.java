package br.com.medeirostec.cms.security;

import br.com.medeirostec.cms.entities.Usuario;

public class JwtUserFactory {

	private JwtUserFactory() {
	}

	/**
	 * Converte e gera um JwtUser com base nos dados de usuario.
	 * 
	 * @param usuario
	 * @return JwtUser
	 */
	public static JwtUser create(Usuario usuario) {
		return new JwtUser(usuario.getId(), usuario.getEmail(), usuario.getSenha());
	}


}
