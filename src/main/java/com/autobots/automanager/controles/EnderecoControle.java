package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import com.autobots.automanager.modelos.endereco.AdicionadorLinkEndereco;
import com.autobots.automanager.modelos.endereco.EnderecoAtualizador;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {
	@Autowired
	private EnderecoRepositorio repositorio;
	@Autowired
	private AdicionadorLinkEndereco adicionadorLinkEndereco;

	@GetMapping("/enderecos")
	public ResponseEntity<List<Endereco>> obterEnderecos() {
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Endereco> enderecos = repositorio.findAll();
		if (enderecos == null) {
			return new ResponseEntity<List<Endereco>>(status);
		} else {
			adicionadorLinkEndereco.adicionarLink(enderecos);
			status = HttpStatus.FOUND;
			return new ResponseEntity<List<Endereco>>(enderecos, status);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<List<Endereco>> obterEndereco(@PathVariable long id) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Endereco> enderecos = repositorio.findAll();
		if (enderecos == null) {
			return new ResponseEntity<List<Endereco>>(status);
		} else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<List<Endereco>>(enderecos, status);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarEndereco(@RequestBody Endereco endereco) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (endereco.getId() == null) {
			repositorio.save(endereco);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarEndereco(@RequestBody Endereco atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Endereco endereco = repositorio.getById(atualizacao.getId());
		if (endereco != null) {
			EnderecoAtualizador atualizador = new EnderecoAtualizador();
			atualizador.atualizar(endereco, atualizacao);
			repositorio.save(endereco);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirEndereco(@RequestBody Endereco exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Endereco endereco = repositorio.getById(exclusao.getId());
		if (endereco != null) {
			repositorio.delete(endereco);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}