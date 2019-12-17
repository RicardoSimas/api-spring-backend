package com.rsimas.cursomc.dto;

import java.io.Serializable;

public class EmailDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	@javax.validation.constraints.NotEmpty(message="Preenchimento obrigatório")
	@javax.validation.constraints.Email(message="Email inválido!")
	private String email;
	
	public EmailDTO() {}
	
	public EmailDTO(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
