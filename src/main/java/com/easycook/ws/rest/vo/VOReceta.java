package com.easycook.ws.rest.vo;

import java.util.List;

public class VOReceta {
	private int id;
	private int likes;
	private int tipo_comida;
	private int id_usuario;
	private int porciones;
	private String nombre;
	private String preparacion;
	private String url_video;
	private List<VOIngrediente> ingredientes;
	private String image;
	//private List<VOImagen> imagenes;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getTipo_comida() {
		return tipo_comida;
	}
	public void setTipo_comida(int tipo_comida) {
		this.tipo_comida = tipo_comida;
	}
	public int getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}
	public int getPorciones() {
		return porciones;
	}
	public void setPorciones(int porciones) {
		this.porciones = porciones;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPreparacion() {
		return preparacion;
	}
	public void setPreparacion(String preparacion) {
		this.preparacion = preparacion;
	}
	public String getUrl_video() {
		return url_video;
	}
	public void setUrl_video(String url_video) {
		this.url_video = url_video;
	}
	public List<VOIngrediente> getIngredientes() {
		return ingredientes;
	}
	public void setIngredientes(List<VOIngrediente> ingredientes) {
		this.ingredientes = ingredientes;
	}
	/*public List<VOImagen> getImagenes() {
		return imagenes;
	}
	public void setImagenes(List<VOImagen> imagenes) {
		this.imagenes = imagenes;
	}*/
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
}
