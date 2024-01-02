package com.gestion.contactos.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.contactos.modelo.LoginRespose;
import com.gestion.contactos.modelo.Usuario;
import com.gestion.contactos.repositorio.UsuarioRepositorio;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginControlador {

    @Autowired
    private UsuarioRepositorio repositorio;

    @PostMapping("/usuario/login")
    public ResponseEntity<LoginRespose> login(@RequestBody Usuario credentials) {
    	LoginRespose response = new LoginRespose();
        String email = credentials.getEmail();
        String password = credentials.getPassword();

        Usuario usuario = repositorio.findByEmailContaining(email);

        if (usuario != null) {
        	if (password.equals(usuario.getPassword())) {                
                response.setSuccess(true);
                response.setMessage("Inicio de sesión exitoso.");
                response.setUsuarioId(usuario.getId());
                response.setName(usuario.getNombre());
                return ResponseEntity.ok(response);
            }
        	else {        		
                response.setSuccess(false);
                response.setMessage("Contraseña incorrecta");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        	}
        }
        else {        	
            response.setSuccess(false);
            response.setMessage("No existe un usuario con ese correo.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
