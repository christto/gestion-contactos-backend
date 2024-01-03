package com.gestion.contactos.modelo;

public class LoginRespose {
	       
    private Long usuarioId;
    
    private String name;
	
	public Long getUsuarioId() {
		return usuarioId;
	}
	
	public void setUsuarioId(Long usuario_id) {
		this.usuarioId = usuario_id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}