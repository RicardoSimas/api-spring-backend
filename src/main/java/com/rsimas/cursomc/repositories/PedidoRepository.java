package com.rsimas.cursomc.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rsimas.cursomc.domain.Cliente;
import com.rsimas.cursomc.domain.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer>{
	
	@org.springframework.transaction.annotation.Transactional(readOnly=true)
	Page<Pedido> findByCliente(Cliente cliente, org.springframework.data.domain.Pageable pageRequest);
}
