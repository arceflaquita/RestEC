package com.easycook.ws.rest.vo;

public class VOIngrediente {
	private String nombre;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public VOIngrediente(String nombre){
		this.nombre = nombre;
	}
}
