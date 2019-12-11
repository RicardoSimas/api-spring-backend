package com.rsimas.cursomc.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import com.rsimas.cursomc.domain.Produto;

public class ProdutoDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	@javax.validation.constraints.NotEmpty(message="Preenchimento obrigatório")
	@Length(min=3, max=50, message="O tamanho deve ser entre 3 e 50 chars.")
	private String nome;
	
	@javax.validation.constraints.NotEmpty(message="Preenchimento obrigatório")
	private double preco;
	
	public ProdutoDTO() {}
	
	public ProdutoDTO(Produto obj) {
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.preco = obj.getPreco();
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

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}
}
