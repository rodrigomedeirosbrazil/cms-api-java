package br.com.medeirostec.cms.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.medeirostec.cms.dtos.CadastroDto;
import br.com.medeirostec.cms.entities.Usuario;
import br.com.medeirostec.cms.services.UsuarioService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CadastroControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UsuarioService usuarioService;

	private static final String URL_BASE = "/cadastro";
	private static final String NOME = "Teste nome";
	private static final String EMAIL = "teste@teste.com";
	private static final String SENHA = "123";

	@Test
	@WithMockUser
	public void testCadastrarUsuario() throws Exception {
		Usuario usuario = obterDadosUsuario();
		BDDMockito.given(this.usuarioService.buscarPorId(Mockito.anyLong())).willReturn(Optional.of(new Usuario()));
		BDDMockito.given(this.usuarioService.persistir(Mockito.any(Usuario.class))).willReturn(usuario);

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.obterJsonRequisicaoPost())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.email").value(EMAIL))
				.andExpect(jsonPath("$.data.nome").value(NOME))
				.andExpect(jsonPath("$.errors").isEmpty());
	}
	
	@Test
	@WithMockUser
	public void testCadastrarUsuarioEmailInvalido() throws Exception {
		BDDMockito.given(this.usuarioService.buscarPorId(Mockito.anyLong())).willReturn(Optional.empty());

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.obterJsonRequisicaoPostEmailInvalido())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Email inválido."))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@WithMockUser
	public void testCadastrarUsuarioSemSenha() throws Exception {
		BDDMockito.given(this.usuarioService.buscarPorId(Mockito.anyLong())).willReturn(Optional.empty());

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.obterJsonRequisicaoPostSemSenha())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Senha não pode ser vazia."))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@WithMockUser
	public void testCadastrarUsuarioSemNome() throws Exception {
		BDDMockito.given(this.usuarioService.buscarPorId(Mockito.anyLong())).willReturn(Optional.empty());

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.obterJsonRequisicaoPostSemNome())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Nome não pode ser vazio."))
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	private Usuario obterDadosUsuario() {
		Usuario usuario = new Usuario();
		usuario.setNome(NOME);
		usuario.setEmail(EMAIL);
		usuario.setSenha(SENHA);
		return usuario;
	}

	private String obterJsonRequisicaoPost() throws JsonProcessingException {
		CadastroDto cadastroDto = new CadastroDto();
		cadastroDto.setId(null);
		cadastroDto.setNome(NOME);
		cadastroDto.setEmail(EMAIL);
		cadastroDto.setSenha(SENHA);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(cadastroDto);
	}
	
	private String obterJsonRequisicaoPostEmailInvalido() throws JsonProcessingException {
		CadastroDto cadastroDto = new CadastroDto();
		cadastroDto.setId(null);
		cadastroDto.setNome(NOME);
		cadastroDto.setEmail("asd123@");
		cadastroDto.setSenha(SENHA);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(cadastroDto);
	}

	private String obterJsonRequisicaoPostSemSenha() throws JsonProcessingException {
		CadastroDto cadastroDto = new CadastroDto();
		cadastroDto.setId(null);
		cadastroDto.setNome(NOME);
		cadastroDto.setEmail(EMAIL);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(cadastroDto);
	}
	
	private String obterJsonRequisicaoPostSemNome() throws JsonProcessingException {
		CadastroDto cadastroDto = new CadastroDto();
		cadastroDto.setId(null);
		cadastroDto.setEmail(EMAIL);
		cadastroDto.setSenha(SENHA);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(cadastroDto);
	}

}
