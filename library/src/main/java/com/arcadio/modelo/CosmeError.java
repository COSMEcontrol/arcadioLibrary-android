package com.arcadio.modelo;

public class CosmeError extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String variable;
	private String msgError;
	
	public CosmeError() {
		super();
	}
	public CosmeError(String variable, String msgError) {
		super();
		this.variable = variable;
		this.msgError = msgError;
	}
	public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
	}
	public String getMsgError() {
		return msgError;
	}
	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}
	
	
}
