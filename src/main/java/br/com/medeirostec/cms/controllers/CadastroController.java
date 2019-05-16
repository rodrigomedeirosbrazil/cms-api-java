package br.com.medeirostec.cms.controllers;

import java.security.NoSuchAlgorithmException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.medeirostec.cms.dtos.CadastroDto;
import br.com.medeirostec.cms.entities.Usuario;
import br.com.medeirostec.cms.response.Response;
import br.com.medeirostec.cms.services.UsuarioService;
import br.com.medeirostec.cms.utils.PasswordUtils;


@RestController
@RequestMapping("/cadastro")
@CrossOrigin(origins = "*")
public class CadastroController {

	private static final Logger log = LoggerFactory.getLogger(CadastroController.class);

	@Autowired
	private UsuarioService usuarioService;

	public CadastroController() {
	}
	
	/**
	 * Cadastra um usuário.
	 * 
	 * @param cadastroDto
	 * @param result
	 * @return ResponseEntity<Response<CadastroPFDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<CadastroDto>> cadastrar(@Valid @RequestBody CadastroDto cadastroDto,
			BindingResult result) throws NoSuchAlgorithmException {
		log.info("Cadastrando usuário: {}", cadastroDto.toString());
		Response<CadastroDto> response = new Response<CadastroDto>();

		validarDadosExistentes(cadastroDto, result);
		Usuario usuario = this.converterDtoParaUsuario(cadastroDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando dados de cadastro do usuário: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.usuarioService.persistir(usuario);

		response.setData(this.converterCadastroDto(usuario));
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Verifica se os dados do usuário
	 * 
	 * @param cadastroDto
	 * @param result
	 */
	private void validarDadosExistentes(CadastroDto cadastroDto, BindingResult result) {
		this.usuarioService.buscarPorEmail(cadastroDto.getEmail())
			.ifPresent(func -> result.addError(new ObjectError("usuario", "Email já existente.")));
	}
	
	/**
	 * Converte os dados do DTO para usuário.
	 * 
	 * @param cadastroDto
	 * @param result
	 * @return Usuario
	 * @throws NoSuchAlgorithmException
	 */
	private Usuario converterDtoParaUsuario(CadastroDto cadastroDto, BindingResult result)
			throws NoSuchAlgorithmException {
		Usuario usuario = new Usuario();
		usuario.setNome(cadastroDto.getNome());
		usuario.setEmail(cadastroDto.getEmail());
		usuario.setSenha(PasswordUtils.gerarBCrypt(cadastroDto.getSenha()));
		return usuario;
	}
	
	/**
	 * Retorna um DTO com os dados de um usuário.
	 * 
	 * @param usuario
	 * @return CadastroDto
	 */
	private CadastroDto converterCadastroDto(Usuario usuario) {
		CadastroDto cadastroDto = new CadastroDto();
		cadastroDto.setId(usuario.getId());
		cadastroDto.setEmail(usuario.getEmail());
		cadastroDto.setNome(usuario.getNome());
		return cadastroDto;
	}
}