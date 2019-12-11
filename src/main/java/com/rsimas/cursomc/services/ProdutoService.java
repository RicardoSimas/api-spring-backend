package com.rsimas.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.rsimas.cursomc.domain.Categoria;
import com.rsimas.cursomc.domain.Produto;
import com.rsimas.cursomc.repositories.CategoriaRepository;
import com.rsimas.cursomc.repositories.ProdutoRepository;

import javassist.tools.rmi.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository catRepo;
	
	public Produto find(Integer id) throws ObjectNotFoundException {
		Optional<Produto> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new com.rsimas.cursomc.services.exception.ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction){
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction),
				orderBy);
		
		List<Categoria> categorias = catRepo.findAllById(ids);
		
		return repo.search(nome, categorias, pageRequest);
		
	}
}
