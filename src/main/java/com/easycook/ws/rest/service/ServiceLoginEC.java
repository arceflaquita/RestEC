package com.easycook.ws.rest.service;

import java.sql.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.easycook.ws.rest.db.Conexion;
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
}
