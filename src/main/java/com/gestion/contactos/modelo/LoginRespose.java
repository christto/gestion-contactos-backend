package com.gestion.contactos.modelo;

public class LoginRespose {
	
    private boolean success;
    
    private String message;
    
    private Long usuarioId;
    
    private String name;

    public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
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