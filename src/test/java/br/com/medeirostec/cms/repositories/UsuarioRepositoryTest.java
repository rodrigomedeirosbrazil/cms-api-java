package br.com.medeirostec.cms.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.NoSuchAlgorithmException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.medeirostec.cms.entities.Usuario;
import br.com.medeirostec.cms.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private static final String EMAIL = "teste@email.com";
	
	@Before
	public void setUp() throws Exception {
		this.usuarioRepository.save(obterDadosUsuario());
	}
	
	@After
    public final void tearDown() { 
		//this.usuarioRepository.deleteAll();
	}
	
	@Test
	public void testBuscarUsuarioPorEmail() {
		Usuario usuario = this.usuarioRepository.findByEmail(EMAIL);
		assertEquals(EMAIL, usuario.getEmail());
	}
	
	private Usuario obterDadosUsuario() throws NoSuchAlgorithmException {
		Usuario usuario = new Usuario();
		usuario.setNome("Teste nome");
		usuario.setEmail(EMAIL);
		usuario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		return usuario;
	}
}