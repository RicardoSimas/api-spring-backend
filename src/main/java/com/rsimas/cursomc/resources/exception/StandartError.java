package com.rsimas.cursomc.resources.exception;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;

public class StandartError implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer status;
	private String msg;
	@JsonFormat(pattern="HH:mm")
	private long timeStamp;
	
	public StandartError(Integer status, String msg, long timeStamp) {
		super();
		this.status = status;
		this.msg = msg;
		this.timeStamp = timeStamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@JsonFormat(pattern="HH:mm")
	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
}
