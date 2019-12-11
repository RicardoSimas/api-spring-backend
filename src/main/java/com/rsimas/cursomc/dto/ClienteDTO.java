package com.rsimas.cursomc.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import com.rsimas.cursomc.domain.Cliente;
import com.rsimas.cursomc.services.validation.ClienteUpdate;

@ClienteUpdate
public class ClienteDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	@javax.validation.constraints.NotEmpty(message="Preenchimento obrigatório")
	@Length(min=5, max=120, message="O tamanho deve ser entre 5 e 120 chars.")
	private String nome;
	
	@javax.validation.constraints.NotEmpty(message="Preenchimento obrigatório")
	@javax.validation.constraints.Email(message="Email inválido!")
	private String email;
	
	public ClienteDTO() {}
	
	public ClienteDTO(Cliente obj) {
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.email = obj.getEmail();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
