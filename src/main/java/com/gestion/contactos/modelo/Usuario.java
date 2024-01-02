package com.gestion.contactos.modelo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nombre", length = 60, nullable = false)
	private String nombre;

	@Column(name = "apellido", length = 60, nullable = false)
	private String apellido;

	@Column(name = "telefono", length = 12, nullable = false, unique = true)
	private String telefono;
	
	@Column(name = "email", length = 100, nullable = false, unique = true)
	private String email;
	
	@Column(name = "password", length = 12, nullable = false)
	private String password;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	@JsonIgnore
    private List<UsuarioContacto> contactos;
	
	public Usuario() {}

	public Usuario(Long id, String nombre, String apellido, String telefono, String email, String password) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public List<UsuarioContacto> getContactos() {
        return contactos;
    }

    public void setContactos(List<UsuarioContacto> contactos) {
        this.contactos = contactos;
    }
	
}
