package com.rsimas.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rsimas.cursomc.domain.Cidade;
import com.rsimas.cursomc.domain.Cliente;
import com.rsimas.cursomc.domain.Endereco;
import com.rsimas.cursomc.domain.enuns.Perfil;
import com.rsimas.cursomc.domain.enuns.TipoCliente;
import com.rsimas.cursomc.dto.ClienteAllDTO;
import com.rsimas.cursomc.dto.ClienteDTO;
import com.rsimas.cursomc.repositories.ClienteRepository;
import com.rsimas.cursomc.repositories.EnderecoRepository;
import com.rsimas.cursomc.security.UserSS;
import com.rsimas.cursomc.services.exception.AuthorizationException;
import com.rsimas.cursomc.services.exception.DataIntregrityException;

import javassist.tools.rmi.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EnderecoRepository repoEnd;
	
	@Autowired
	private S3Service s3Service; 
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.profile.size}")
	private Integer size;
	
	public Cliente find(Integer id) throws ObjectNotFoundException {
		UserSS user = UserService.authenticated();
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso Negado!");
		}
		
		Optional<Cliente> obj = repo.findById(id);
				
		return obj.orElseThrow(() -> new com.rsimas.cursomc.services.exception.ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		repoEnd.saveAll(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) throws ObjectNotFoundException {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) throws ObjectNotFoundException {
		find(id);
		try {
			repo.deleteById(id);
		}catch(DataIntegrityViolationException e) {
			throw new DataIntregrityException("Não é possível excluir este Cliente!");
		}
	}
	
	public List<Cliente> findAll(){
		
		List<Cliente> categorias = new ArrayList<Cliente>(); 
		
		categorias = repo.findAll();
		
		return categorias;
	}
	
	public Cliente findByEmail(String email) throws ObjectNotFoundException {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}
	
		Cliente obj = repo.findByEmail(email);
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Cliente.class.getName());
		}
		return obj;
	}
	
	//Método que Encapsula informações e operações sobre a paginação. 
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction),
				orderBy);
		
		return repo.findAll(pageRequest);
	}
	
	public Cliente FromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null);
	}
	
	public Cliente FromDTO(ClienteAllDTO objDTO) {
		Cliente cli = new Cliente(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOuCnpj(), TipoCliente.toEnum(objDTO.getTipo()), pe.encode(objDTO.getSenha()));
		Cidade cid = new Cidade(objDTO.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(), objDTO.getBairro(), objDTO.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDTO.getTelefone1());
			
		if(objDTO.getTelefone2()!= null) {
			cli.getTelefones().add(objDTO.getTelefone2());
		}
		
		if(objDTO.getTelefone3()!= null) {
			cli.getTelefones().add(objDTO.getTelefone3());
		}
		
		return cli;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		jpgImage = imageService.cropSquare(jpgImage);
		jpgImage = imageService.resize(jpgImage, size);
		
		String fileName = prefix + user.getId() + ".jpg";
		
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
	}
	
	
}
