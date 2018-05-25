package com.easycook.ws.rest.service;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
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
    Conexion conecta = new Conexion();
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
	        
	        /*
			sentencia= "SELECT correo, AES_DECRYPT(contrasenia,'yahooo'),id_usuario,nombre,ap_paterno,ap_materno"
	                + " FROM usuarios "
                + " WHERE correo = '" + user.getCorreo() + "'";
       //         + " AND contrasenia = '" + user.getPassword() + "'";
        
        */
	        sentencia= "SELECT correo, AES_DECRYPT(contrasenia,'yahooo'),id_usuario,nombre,ap_paterno,ap_materno"
	                + " FROM usuarios "
                + " WHERE correo = '" + user.getCorreo() + "'"
	        	+ " AND contrasenia = '" + user.getPassword() + "'";


			cdr = sentenciaSQL.executeQuery(sentencia);
			if(cdr.first()){
				/*if(user.getCorreo().equals(cdr.getString("correo")) && user.getPassword().equals(cdr.getString(2))){
					user.setUserValido(true);
					user.setNombre(cdr.getString("nombre"));
					user.setAp_paterno(cdr.getString("ap_paterno"));
					user.setAp_materno(cdr.getString("ap_materno"));					
				}
				if(user.getCorreo().equals(cdr.getString("correo"))){
					user.setCorreoIgual(true);
				}*/
				user.setUserValido(true);
				user.setNombre(cdr.getString("nombre"));
				user.setAp_paterno(cdr.getString("ap_paterno"));
				user.setAp_materno(cdr.getString("ap_materno"));
				user.setCorreoIgual(true);				
				user.setIdUsuario(cdr.getInt("id_usuario"));
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
		 		+ " AES_ENCRYPT('"+user.getPassword()+"','yahooo'), now());";
	    
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
		        
				sentencia= "SELECT id_receta,nombre_receta, modo_preparacion, " + 
				" url_video, id_usuario, id_tipo, porciones, likes " +
		        " FROM recetas " +
				" WHERE nombre_receta LIKE '%" + rec.getNombre() + "%'";
				
				cdr = sentenciaSQL.executeQuery(sentencia);
				while(cdr.next()){
					VOReceta newRec = new VOReceta();
					newRec.setId(cdr.getInt("id_receta"));
					newRec.setImage("receta_" + cdr.getString("id_receta") + ".jpg");
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
        byte[] decodedBytes = Base64.getMimeDecoder().decode(input);
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
				byte[] ba = decodeBase64(receta.getImage());
				try{
						FileOutputStream fos = new FileOutputStream("C:\\apache\\apache-tomcat-9.0.8\\webapps\\RestEC\\images\\receta_" + String.valueOf(receta_id) + ".jpg");
					try {
					    fos.write(ba);
					}				
					finally {
					    fos.close();
					}
				}
				catch (IOException io){				
				}				
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
	
	

	@POST
	@Path("/consultaEspecifca")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public  List<VOReceta> consaEspecifica(VOReceta receta){
		List<VOReceta> result = new ArrayList<VOReceta>();
		try {
			String nomReceta="nombre_ingrediente LIKE '%";
			String ConsultaBD="nombre_ingrediente LIKE '%";
			String complemento=" || ";
			int contador=1;
			if(receta.getIngredientes().size()==1){
				for(VOIngrediente ing: receta.getIngredientes()){
					ConsultaBD+=ing.getNombre()+"%' ";
					System.out.println("sentencia SQL:  "+ConsultaBD);
						
					}
			}else{
				for(VOIngrediente ing: receta.getIngredientes()){
					if(contador==receta.getIngredientes().size()){
						ConsultaBD+=" "+ing.getNombre()+"%' " ;
					}else{
						ConsultaBD+=" "+ing.getNombre()+"%' "+complemento+nomReceta ;
					}
					
					System.out.println("sentencia SQL:  "+ConsultaBD);
						
						contador+=1;
					}
			}
			//crea una conexion con mariadb
			conecta.Conectar();
	        sentenciaSQL = conecta.getSentenciaSQL();
	        //" inner join ingredientes on receta_ingredientes.id_ingrediente = ingredientes.id_ingrediente"+
			sentencia= "select receta_ingredientes.id_receta , nombre_receta from receta_ingredientes "+
			           " inner join recetas on receta_ingredientes.id_receta=recetas.id_receta"+					   
					   " where "+ConsultaBD+"group by receta_ingredientes.id_receta";
			System.out.println("sentencia SQL:  "+sentencia);
			
			cdr = sentenciaSQL.executeQuery(sentencia);
			while(cdr.next()){
				VOReceta newRec = new VOReceta();
				newRec.setId(cdr.getInt("id_receta"));
				newRec.setNombre(cdr.getString("nombre_receta"));
				newRec.setImage("receta_" + cdr.getString("id_receta") + ".jpg");
				result.add(newRec);
			}
			System.out.println("lista: "+result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;	
	}
	
	@POST
	@Path("/consRecetaTipo")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public List<VOReceta> consRecetaTipo(VOReceta rec){
		List<VOReceta> result = new ArrayList<VOReceta>();
		try {
			//crea una conexion con mariadb
			conecta.Conectar();
	        sentenciaSQL = conecta.getSentenciaSQL();
	        
			sentencia= "SELECT id_receta,nombre_receta " +
	        " FROM recetas " +
			" WHERE id_tipo="+rec.getTipo_comida();
			
			cdr = sentenciaSQL.executeQuery(sentencia);
			while(cdr.next()){
				VOReceta newRec = new VOReceta();
				newRec.setId(cdr.getInt("id_receta"));
				newRec.setNombre(cdr.getString("nombre_receta"));
				newRec.setImage("receta_" + cdr.getString("id_receta") + ".jpg");
				result.add(newRec);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;	
	}
	
	@POST
	@Path("/listarRecetas")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public List<VOReceta> listarRecetas(){
		List<VOReceta> result = new ArrayList<VOReceta>();
		try {
			//crea una conexion con mariadb
			conecta.Conectar();
	        sentenciaSQL = conecta.getSentenciaSQL();
	        
			sentencia = "SELECT id_receta, nombre_receta FROM recetas";
			
			cdr = sentenciaSQL.executeQuery(sentencia);
			while(cdr.next()){
				VOReceta newRec = new VOReceta();
				newRec.setId(cdr.getInt("id_receta"));
				newRec.setNombre(cdr.getString("nombre_receta"));
				newRec.setImage("receta_" + cdr.getString("id_receta") + ".jpg");
				result.add(newRec);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;	
	}
	
	
	@POST
	@Path("/recetaDetalle")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public VOReceta recetaDetalle(VOReceta rec){
		VOReceta newRec = null;
		try {
			//crea una conexion con mariadb
			conecta.Conectar();
	        sentenciaSQL = conecta.getSentenciaSQL();
	        
			sentencia = "SELECT id_receta, nombre_receta, modo_preparacion, " +
			" url_video, tipo_comida, porciones " +
			" FROM recetas r " +
			" INNER JOIN tipo_comida tc ON " + 
			" r.id_tipo = tc.id_tipo " +
			" where id_receta = " + rec.getId();
			
			cdr = sentenciaSQL.executeQuery(sentencia);			
			if(cdr.first()){
				newRec = new VOReceta();
				newRec.setId(cdr.getInt("id_receta"));
				newRec.setNombre(cdr.getString("nombre_receta"));
				newRec.setImage("receta_" + cdr.getString("id_receta") + ".jpg");
				newRec.setPreparacion(cdr.getString("modo_preparacion"));
				newRec.setPorciones(Integer.parseInt(cdr.getString("porciones")));
				newRec.setUrl_video(cdr.getString("url_video"));
				newRec.setComida(cdr.getString("tipo_comida"));
			}
			
			sentencia = "SELECT id_receta, nombre_ingrediente from receta_ingredientes " +
					" where id_receta = " + rec.getId();
			cdr = sentenciaSQL.executeQuery(sentencia);
			List<VOIngrediente> ings = new ArrayList<VOIngrediente>();
			while(cdr.next()){
				ings.add(new VOIngrediente(cdr.getString("nombre_ingrediente")));
			}
			
			sentencia="SELECT id_usuario from recetas_favoritas "
					+ " where id_usuario="+rec.getId_usuario()+"&& id_receta="+rec.getId()+"";
			cdr = sentenciaSQL.executeQuery(sentencia);	
			if (cdr.next()) {
				
					newRec.setFavoritaUser(true);
				}else{
					newRec.setFavoritaUser(false);
				}
				
			
			newRec.setIngredientes(ings);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return newRec;
		}
		return newRec;	
	}
	
	 @POST
		@Path("/recuperarPassword")
		@Consumes({MediaType.APPLICATION_JSON})
		@Produces({MediaType.APPLICATION_JSON})
		public VOUsuario recuperarPassword(VOUsuario user){
			
			
			try {
				//crea una conexion con mariadb
				conecta.Conectar();
		        sentenciaSQL = conecta.getSentenciaSQL();
		        
				sentencia= "SELECT contrasenia,nombre,ap_paterno,ap_materno "
		                + " FROM usuarios "
	                + " WHERE correo = '" + user.getCorreo() + "'";
	       //         + " AND contrasenia = '" + user.getPassword() + "'";


				cdr = sentenciaSQL.executeQuery(sentencia);
				if(cdr.first()){
					
					    String to =user.getCorreo();
							 
		                String subject = "Recuperaci�n de password";
		                
		                String message ="Estimado "+cdr.getString(2)+" "+cdr.getString(3)+""
		                		+ "Su contrase�a es: "+cdr.getString(1);
		                /* String message ="<html> <body><div style='border: 5px solid black;'><img src='http://127.0.0.1:8080/RestEC/images/easy.jpg' style=' width: 150px; margin-left: 50%;'/>"
                		+"<h2>Estimado:"+cdr.getString(2)+" "+cdr.getString(3)+""+cdr.getString(4)+" </h2> </br> <h2>Su contrase�a es :"+cdr.getString(1)+" </h2> </div> </body> </html> ";
                		*/
    		            
		                String correo = "easycooklab@gmail.com";
		                String pass = "vqprlchpjlqaxnkh";
					
					SendMail.send(to, subject, message, correo, pass);
					
				
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return user;		


		}
	 
		@POST
		@Path("/recetasUser")
		@Consumes({MediaType.APPLICATION_JSON})
		@Produces({MediaType.APPLICATION_JSON})
		public List<VOReceta> recetasFavoritaUser(VOReceta rec){
			List<VOReceta> result = new ArrayList<VOReceta>();
			try {
				//crea una conexion con mariadb
				conecta.Conectar();
		        sentenciaSQL = conecta.getSentenciaSQL();
		        
				sentencia= "SELECT id_receta,nombre_receta " +
		        " FROM recetas " +
				" WHERE id_receta in(select id_receta from recetas_favoritas where id_usuario="+rec.getId_usuario()+");";
				
				cdr = sentenciaSQL.executeQuery(sentencia);
				while(cdr.next()){
					VOReceta newRec = new VOReceta();
					newRec.setId(cdr.getInt("id_receta"));
					newRec.setNombre(cdr.getString("nombre_receta"));
					newRec.setImage("receta_" + cdr.getString("id_receta") + ".jpg");
					result.add(newRec);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;	
		}
	
		
		@POST
		@Path("/agregarFavorita")
		@Consumes({MediaType.APPLICATION_JSON})
		@Produces({MediaType.APPLICATION_JSON})
		public VOReceta agregarFavorita(VOReceta receta){
			if(receta.getId_usuario() == 0) receta.setId_usuario(1);
			sentencia = "INSERT INTO `recetas_favoritas`(`id_usuario`, `id_receta`) VALUES ("+receta.getId_usuario()+","+receta.getId()+")";    
			try {
				//crea una conexion con mariadb
				conecta.Conectar();
		        sentenciaSQL = conecta.getSentenciaSQL();	        
				sentenciaSQL.executeUpdate(sentencia);
				receta.setFavoritaUser(true);
					
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();	
				return receta;
			}		
			return receta;
		}
}
