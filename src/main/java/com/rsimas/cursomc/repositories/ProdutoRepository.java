package com.rsimas.cursomc.repositories;

	
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rsimas.cursomc.domain.Categoria;
import com.rsimas.cursomc.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer>{
	
	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	public Page<Produto> search(@Param("nome") String nome,@Param("categorias") List<Categoria> categorias, Pageable pageRequest);
	
	//OUUUU 
	//Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
	
}
