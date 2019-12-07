package com.rsimas.cursomc.services.exception;

public class DataIntregrityException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public DataIntregrityException(String msg) {
		super(msg);
	}
	
	public DataIntregrityException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
