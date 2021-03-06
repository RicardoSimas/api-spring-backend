package com.rsimas.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.rsimas.cursomc.domain.Cliente;
import com.rsimas.cursomc.domain.enuns.TipoCliente;
import com.rsimas.cursomc.dto.ClienteAllDTO;
import com.rsimas.cursomc.repositories.ClienteRepository;
import com.rsimas.cursomc.resources.exception.FieldMessage;
import com.rsimas.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteAllDTO> {
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClienteAllDTO objDTO, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		if(objDTO.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDTO.getCpfOuCnpj())) {
			list.add(new FieldMessage("CpfOuCnpj", "CPF Inválido!"));
		}
		
		if(objDTO.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDTO.getCpfOuCnpj())) {
			list.add(new FieldMessage("CpfOuCnpj", "CNPJ Inválido!"));
		}
		
		Cliente aux = repo.findByEmail(objDTO.getEmail());
		
		if(aux != null) {
			list.add(new FieldMessage("email", "Email Existente!"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}