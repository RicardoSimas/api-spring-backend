package com.rsimas.cursomc.resources.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandartError {
	private static final long serialVersionUID = 1L;
	
	private List<FildMessage> errors = new ArrayList<FildMessage>();
	
	public ValidationError(Integer status, String msg, long timeStamp) {
		super(status, msg, timeStamp);
		
	}
	public List<FildMessage> getErrors() {
		return errors;
	}
	
	public void addError(String fildName, String message) {
		errors.add(new FildMessage(fildName, message));
	}

}
