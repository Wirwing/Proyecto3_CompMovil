package com.fmat.proyecto3.json;

import com.google.gson.annotations.SerializedName;

public class Exercise {

	private String id;
	
	@SerializedName("titulo")
	private String title;
	
	@SerializedName("descripcion")
	private String description;
	
	@SerializedName("sentencias")
	private String[] statements;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String titulo) {
		this.title = titulo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getStatements() {
		return statements;
	}

	public void setStatements(String[] statements) {
		this.statements = statements;
	}

	
	
}
