package br.com.medeirostec.cms.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.medeirostec.cms.entities.Usuario;
import br.com.medeirostec.cms.repositories.UsuarioRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@MockBean
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CadastroService cadastroService;

	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.usuarioRepository.save(Mockito.any(Usuario.class))).willReturn(new Usuario());
		BDDMockito.given(this.usuarioRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Usuario()));
		BDDMockito.given(this.usuarioRepository.findByEmail(Mockito.anyString())).willReturn(new Usuario());
	}

	@Test
	public void testPersistirUsuario() {
		Usuario usuario = this.cadastroService.persistir(new Usuario());

		assertNotNull(usuario);
	}

	@Test
	public void testBuscarUsuarioPorId() {
		Optional<Usuario> usuario = this.cadastroService.buscarPorId(1L);

		assertTrue(usuario.isPresent());
	}

	@Test
	public void testBuscarUsuarioPorEmail() {
		Optional<Usuario> usuario = this.cadastroService.buscarPorEmail("email@email.com");

		assertTrue(usuario.isPresent());
	}
}