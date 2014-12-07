package com.arcadio.api.v1.service.exceptions;

/**
 * Created by alberto on 7/12/14.
 */
public class ServiceDisconnectedArcadioException extends Exception{
	private String msg;

	/** Creates a new instance of NoConnectedArcadioException */
	public ServiceDisconnectedArcadioException(String _msg) {
		this.msg = _msg;
	}

	/** Creates a new instance of NoConnectedArcadioException */
	public ServiceDisconnectedArcadioException() {
	}

	public String getMessage(){
		return this.msg;
	}

	public String toString(){
		return this.msg;
	}
}
