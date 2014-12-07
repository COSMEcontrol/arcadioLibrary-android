package com.arcadio.api.v1.service.exceptions;

/**
 * Created by alberto on 7/12/14.
 */
public class NoConnectedArcadioException extends Exception{
	private String msg;

	/** Creates a new instance of NoConnectedArcadioException */
	public NoConnectedArcadioException(String _msg) {
		this.msg = _msg;
	}

	/** Creates a new instance of NoConnectedArcadioException */
	public NoConnectedArcadioException() {
	}

	public String getMessage(){
		return this.msg;
	}

	public String toString(){
		return this.msg;
	}
}
