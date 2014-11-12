package com.arcadio.modelo;



public class Perfil {
	/**
	 * Define las constantes necesarias para crear un ContentProvider.
	 * @author alberto
	 *
	 */
		
	public int id;


//	public int orden;
	private String nombre;


	private String ip;
	private int puerto;
	private String password;
	
	
	public static final int PUERTO_POR_DEFECTO = 15150;
	public static final String CONTRASEÑA_POR_DEFECTO = "GTA70";
	
	public Perfil(){
		
	}
	
	public Perfil(String nombre, String ip, int puerto,
			String password) {
		this.nombre = nombre;
		this.ip = ip;
		this.puerto = puerto;
		this.password = password;
	}
	public Perfil(String _nombre){
		this.nombre= _nombre;
	}

//	public Perfil(int orden, String nombre, String ip, int puerto,
//			String password) {
//		this.orden = orden;
//		this.nombre = nombre;
//		this.ip = ip;
//		this.puerto = puerto;
//		this.password = password;
//	}

	public Perfil(String nombre, String ip, String puerto, String password) {
		this.nombre = nombre;
		this.ip = ip;
		
		//comprobar si vienen vacios para poner los valores por defecto
		if(puerto.isEmpty()){
			this.puerto = PUERTO_POR_DEFECTO;
		}else{
			this.puerto = Integer.valueOf(puerto);
		}
		if(password.isEmpty()){
			this.password = CONTRASEÑA_POR_DEFECTO;
		}else{
			this.password = password;
		}
		
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
