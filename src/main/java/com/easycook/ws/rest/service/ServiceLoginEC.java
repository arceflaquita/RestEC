package com.easycook.ws.rest.service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.easycook.ws.rest.db.Conexion;
import com.easycook.ws.rest.vo.VOIngrediente;
import com.easycook.ws.rest.vo.VOReceta;
import com.easycook.ws.rest.vo.VOTipoComida;
import com.easycook.ws.rest.vo.VOUsuario;

@Path("/EasyCook")
public class ServiceLoginEC {
	ResultSet cdr = null;
    Statement sentenciaSQL=null;
    Conexion conecta = new Conexion();;
    String sentencia = "";
	
	@POST
	@Path("/validaUsuario")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public VOUsuario validaUsuario(VOUsuario user){
		user.setUserValido(false);
		
		try {
				//crea una conexion con mariadb
				conecta.Conectar();
		        sentenciaSQL = conecta.getSentenciaSQL();
		        
				sentencia= "SELECT correo, contrasenia "
		                + " FROM usuarios "
	                + " WHERE correo = '" + user.getCorreo() + "'"
	                + " AND contrasenia = '" + user.getPassword() + "'";


				cdr = sentenciaSQL.executeQuery(sentencia);
				if(cdr.first()){
					if(user.getCorreo().equals(cdr.getString("correo")) && user.getPassword().equals(cdr.getString("contrasenia"))){
						user.setUserValido(true);
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return user;		
	}

	@POST
	@Path("/nuevoUsuario")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public VOUsuario nuevoUsuario(VOUsuario user){
		user.setUserValido(false);
		
		sentencia = "INSERT INTO usuarios (nombre, ap_paterno, ap_materno, correo, contrasenia, fecha_registro) " + 
		" VALUES ('"+user.getNombre()+"', '"+user.getAp_paterno()+"', '"+user.getAp_materno()+"', '"+user.getCorreo()+"',"
		 		+ " '"+user.getPassword()+"', now());";
	    
		try {
			//crea una conexion con mariadb
			conecta.Conectar();
	        sentenciaSQL = conecta.getSentenciaSQL();
	        
			sentenciaSQL.executeUpdate(sentencia);
			user.setUserValido(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
		
		return user;
	}
	
	@POST
	@Path("/consTipoComida")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public List<VOTipoComida> consTipoComida(){
		List<VOTipoComida> result = new ArrayList<VOTipoComida>();
		try {
				//crea una conexion con mariadb
				conecta.Conectar();
		        sentenciaSQL = conecta.getSentenciaSQL();
		        
				sentencia= "SELECT id_tipo, tipo_comida "
		                + " FROM tipo_comida ";
				
				cdr = sentenciaSQL.executeQuery(sentencia);
				while(cdr.next()){
					result.add(new VOTipoComida(Integer.parseInt(cdr.getString("id_tipo")), cdr.getString("tipo_comida")));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return result;		
	}
	
	@POST
	@Path("/consReceta")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public List<VOReceta> consReceta(VOReceta rec){
		List<VOReceta> result = new ArrayList<VOReceta>();
		try {
				//crea una conexion con mariadb
				conecta.Conectar();
		        sentenciaSQL = conecta.getSentenciaSQL();
		        
				sentencia= "SELECT nombre_receta, modo_preparacion, " + 
				" url_video, id_usuario, id_tipo, porciones, likes " +
		        " FROM recetas " +
				" WHERE nombre_receta LIKE '%" + rec.getNombre() + "%'";
				
				cdr = sentenciaSQL.executeQuery(sentencia);
				while(cdr.next()){
					VOReceta newRec = new VOReceta();
					newRec.setNombre(cdr.getString("nombre_receta"));
					newRec.setPreparacion(cdr.getString("modo_preparacion"));
					result.add(newRec);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return result;		
	}

    public byte[] decodeBase64(String input)
    {
    	Decoder dec = Base64.getDecoder();
        byte[] decodedBytes = dec.decode(input);
        return decodedBytes;
    }
	
	@POST
	@Path("/nuevaReceta")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public VOReceta nuevaReceta(VOReceta receta){
		if(receta.getId_usuario() == 0) receta.setId_usuario(1);
		sentencia = "INSERT INTO recetas (nombre_receta, modo_preparacion, url_video, fecha_registro, id_usuario, id_tipo, porciones) " + 
		" VALUES ('"+receta.getNombre()+"', '"+receta.getPreparacion()+"', '"+receta.getUrl_video()+"', now(), "+receta.getId_usuario()+", "
		 		+ receta.getTipo_comida()+", "+receta.getPorciones()+");";	    
		try {
			
			byte[] ba = decodeBase64(receta.getImage());
			try{
				FileOutputStream fos = new FileOutputStream("C:\\tmp\\image.jpg");
				try {
				    fos.write(ba);
				}				
				finally {
				    fos.close();
				}
			}
			catch (IOException io){				
			}
			
			//crea una conexion con mariadb
			conecta.Conectar();
	        sentenciaSQL = conecta.getSentenciaSQL();	        
			sentenciaSQL.executeUpdate(sentencia);
			int receta_id = 0;
			sentencia = "SELECT max(id_receta) as id_receta FROM recetas";       
			cdr = sentenciaSQL.executeQuery(sentencia);
			if(cdr.first()){
				receta_id = Integer.parseInt(cdr.getString("id_receta"));
			}
			if(receta_id > 0){
				for(VOIngrediente ing: receta.getIngredientes()){
					sentencia = "INSERT INTO receta_ingredientes (id_receta, nombre_ingrediente) values ("+
								receta_id + ", '" + ing.getNombre()+ "')"; 
					sentenciaSQL.executeUpdate(sentencia);
				}
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
			return receta;
		}		
		return receta;
	}
}
