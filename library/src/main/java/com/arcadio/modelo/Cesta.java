package com.arcadio.modelo;

public class Cesta {



	private int id;
	private String nombre;
	private int periodoRefresco;
//	private Perfil perfil;
//	private ArrayList<ItemVariable> variables;
	

	public Cesta (String nomCesta){
		this.nombre=nomCesta;
	}

	public Cesta() {
	}

	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getPeriodoRefresco() {
		return periodoRefresco;
	}

	public void setPeriodoRefresco(int periodoRefresco) {
		this.periodoRefresco = periodoRefresco;
	}

	
	
	
}
