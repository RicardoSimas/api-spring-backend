package com.rsimas.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.rsimas.cursomc.domain.Categoria;
import com.rsimas.cursomc.repositories.CategoriaRepository;
import com.rsimas.cursomc.services.exception.DataIntregrityException;

import javassist.tools.rmi.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	//Ants para injetar uma instância do tipo CategoriaRepository.
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria find(Integer id) throws ObjectNotFoundException {
		Optional<Categoria> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new com.rsimas.cursomc.services.exception.ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
		}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) throws ObjectNotFoundException {
		find(obj.getId());
		return repo.save(obj);
	}
	
	public void delete(Integer id) throws ObjectNotFoundException {
		find(id);
		try {
			repo.deleteById(id);
		}catch(DataIntegrityViolationException e) {
			throw new DataIntregrityException("Não é possível excluir uma categoria que possui produtos.");
		}
	}
}
