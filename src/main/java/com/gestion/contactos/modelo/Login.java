package com.gestion.contactos.modelo;

public class Login {
	
    private boolean success;
    
    private String message;
    
    private Long usuario_id;

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
		return usuario_id;
	}
	
	public void setUsuarioId(Long usuario_id) {
		this.usuario_id = usuario_id;
	}
	
}