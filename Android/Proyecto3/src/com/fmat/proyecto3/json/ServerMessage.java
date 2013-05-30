package com.fmat.proyecto3.json;

import com.google.gson.annotations.SerializedName;

/**
 * Bean que contiene la respuesta del servidor cuando se envia
 * un ejercicio resuelto.
 * @author Irving
 *
 */
public class ServerMessage {

	public static final String EXTRA_SERVER_MESSAGE = "EXTRA_SERVER_MESSAGE";
	
	@SerializedName("esCorrecto")
	private boolean isCorrect;

	@SerializedName("mensaje")
	private String message;

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

}
