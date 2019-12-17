package com.rsimas.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.rsimas.cursomc.domain.Cliente;
import com.rsimas.cursomc.domain.ItemPedido;
import com.rsimas.cursomc.domain.PagamentoComBoleto;
import com.rsimas.cursomc.domain.Pedido;
import com.rsimas.cursomc.domain.enuns.EstadoPagamento;
import com.rsimas.cursomc.repositories.ItemPedidoRepository;
import com.rsimas.cursomc.repositories.PagamentoRepository;
import com.rsimas.cursomc.repositories.PedidoRepository;
import com.rsimas.cursomc.security.UserSS;
import com.rsimas.cursomc.services.exception.AuthorizationException;

import javassist.tools.rmi.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido find(Integer id) throws ObjectNotFoundException {
		Optional<Pedido> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new com.rsimas.cursomc.services.exception.ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	public Pedido insert(Pedido obj) throws ObjectNotFoundException{
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
	
	//Método que Encapsula informações e operações sobre a paginação. 
		public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) throws ObjectNotFoundException{
			
			UserSS user = UserService.authenticated();
			if(user == null) {
				throw new AuthorizationException("Acesso Negado!");
			}
			
			PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction),
					orderBy);
			
			Cliente cliente = clienteService.find(user.getId());
			
			return repo.findByCliente(cliente, pageRequest);
		}
}
