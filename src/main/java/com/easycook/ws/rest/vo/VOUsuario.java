package com.easycook.ws.rest.vo;

public class VOUsuario {
	private int idUsuario;
	private String usuario;
	private String password;
	private boolean userValido;
	private String nombre;
	private String ap_paterno;
	private String ap_materno;
	private String correo;
	private boolean correoIgual;
	private boolean passwordIgual;
	
	
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	public boolean isPasswordIgual() {
		return passwordIgual;
	}
	public void setPasswordIgual(boolean passwordIgual) {
		this.passwordIgual = passwordIgual;
	}
	public boolean isCorreoIgual() {
		return correoIgual;
	}
	public void setCorreoIgual(boolean correoIgual) {
		this.correoIgual = correoIgual;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isUserValido() {
		return userValido;
	}
	public void setUserValido(boolean userValido) {
		this.userValido = userValido;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getAp_paterno() {
		return ap_paterno;
	}
	public void setAp_paterno(String ap_paterno) {
		this.ap_paterno = ap_paterno;
	}
	public String getAp_materno() {
		return ap_materno;
	}
	public void setAp_materno(String ap_materno) {
		this.ap_materno = ap_materno;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
}
