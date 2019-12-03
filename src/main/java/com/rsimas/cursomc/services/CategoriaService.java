package com.rsimas.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rsimas.cursomc.domain.Categoria;
import com.rsimas.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	//Objeto de dependência
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria search(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		
		return obj.orElse(null);
	}
	 
}
