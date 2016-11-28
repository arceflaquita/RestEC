package com.easycook.ws.rest.vo;

public class VOTipoComida {
	private int idTipoComida;
	private String nombreTipoComida;	

	public VOTipoComida(){		
	}	
	public VOTipoComida(int idTipoComida, String nombreTipoComida){	
		this.idTipoComida = idTipoComida;
		this.nombreTipoComida = nombreTipoComida;
	}
	
	/**
	 * @return the idTipoComida
	 */
	public int getIdTipoComida() {
		return idTipoComida;
	}
	/**
	 * @param idTipoComida the idTipoComida to set
	 */
	public void setIdTipoComida(int idTipoComida) {
		this.idTipoComida = idTipoComida;
	}
	/**
	 * @return the nombreTipoComida
	 */
	public String getNombreTipoComida() {
		return nombreTipoComida;
	}
	/**
	 * @param nombreTipoComida the nombreTipoComida to set
	 */
	public void setNombreTipoComida(String nombreTipoComida) {
		this.nombreTipoComida = nombreTipoComida;
	}
}
