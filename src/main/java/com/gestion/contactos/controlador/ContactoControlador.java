package com.gestion.contactos.controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.contactos.excepciones.ResourceNotFoundException;
import com.gestion.contactos.modelo.Contacto;
import com.gestion.contactos.modelo.Usuario;
import com.gestion.contactos.modelo.UsuarioContacto;
import com.gestion.contactos.repositorio.ContactoRepositorio;
import com.gestion.contactos.repositorio.UsuarioContactoRepositorio;
import com.gestion.contactos.repositorio.UsuarioRepositorio;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "http://localhost:4200")
public class ContactoControlador {

	@Autowired
	private ContactoRepositorio contactoRepositorio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private UsuarioContactoRepositorio usuarioContactoRepositorio;

	//Método para listar todos los contactos
	@GetMapping("/contactos/{usuarioId}")
	public List<Contacto> listarTodosLosContactos(@PathVariable Long usuarioId) {
		 
		Usuario usuario = usuarioRepositorio.getById(usuarioId);
		
		return usuario.getContactos()
				.stream()
				.map(UsuarioContacto::getContacto)
				.collect(Collectors.toList());
	}	

	//Método para guardar el contacto
	@PostMapping("/contactos/{usuarioId}")
    public Contacto guardarContacto(@PathVariable Long usuarioId, @RequestBody Contacto contacto) {
		
		Usuario usuario = usuarioRepositorio.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con el ID: " + usuarioId));

	    // Asigna el usuario al contacto
	    UsuarioContacto usuarioContacto = new UsuarioContacto(usuarioId, usuario, contacto);

	    // Guarda primero el contacto para asegurarse de que esté persistido
	    contactoRepositorio.save(contacto);

	    // Asigna el contacto persistido a UsuarioContacto
	    usuarioContacto.setContacto(contacto);

	    // Asigna UsuarioContacto al usuario
	    usuario.getContactos().add(usuarioContacto);

	    // Guarda el usuario
	    usuarioRepositorio.save(usuario);

	    return contacto;
	    
    }
    
	//Método para buscar un contacto por id
	@GetMapping("/contacto/{contactoId}")
	public ResponseEntity<Contacto> obtenerContactoPorId(@PathVariable Long contactoId) {
	    Optional<Contacto> contactoOptional = contactoRepositorio.findById(contactoId);

	    if (contactoOptional.isPresent()) {
	        return ResponseEntity.ok(contactoOptional.get());
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	//Método para actualizar contacto
	@PutMapping("/contactos/{contactoId}")
	public ResponseEntity<Contacto> actualizarContacto(@PathVariable Long contactoId,@RequestBody Contacto detallesContacto){
		Contacto contacto = contactoRepositorio.findById(contactoId)
				            .orElseThrow(() -> new ResourceNotFoundException("No existe el contacto con el ID : " + contactoId));
		
		contacto.setNombre(detallesContacto.getNombre());
		contacto.setApellido(detallesContacto.getApellido());
		contacto.setTelefono(detallesContacto.getTelefono());
		
		Contacto contactoActualizado = contactoRepositorio.save(contacto);
		return ResponseEntity.ok(contactoActualizado);
    }
	
	//Método para eliminar un contacto
	@DeleteMapping("/contactos/{usuarioId}/{contactoId}")
	public ResponseEntity<Map<String, Boolean>> eliminarContactoYRelacion(
	        @PathVariable Long usuarioId,
	        @PathVariable Long contactoId) {
	    // Primero, verifica si el usuario existe
	    Usuario usuario = usuarioRepositorio.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con el ID : " + usuarioId));

	    // Luego, verifica si el contacto existe
	    Contacto contacto = contactoRepositorio.findById(contactoId)
	            .orElseThrow(() -> new ResourceNotFoundException("No existe el contacto con el ID : " + contactoId));

	    // Verifica si el contacto está asociado al usuario
	    UsuarioContacto usuarioContacto = usuarioContactoRepositorio
	            .findByUsuarioAndContacto(usuario, contacto)
	            .orElseThrow(() -> new ResourceNotFoundException("El contacto no está asociado al usuario"));

	    // Elimina la relación usuario-contacto
	    usuarioContactoRepositorio.delete(usuarioContacto);

	    // Elimina el contacto
	    contactoRepositorio.delete(contacto);

	    Map<String, Boolean> respuesta = new HashMap<>();
	    respuesta.put("eliminar", Boolean.TRUE);
	    return ResponseEntity.ok(respuesta);
	}

}
