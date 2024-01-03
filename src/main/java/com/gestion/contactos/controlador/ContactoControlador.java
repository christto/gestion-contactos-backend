package com.gestion.contactos.controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.contactos.modelo.ApiResponse;
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
	public ApiResponse<List<Contacto>> listarTodosLosContactos(@PathVariable Long usuarioId) {
	    ApiResponse<List<Contacto>> response = new ApiResponse<>();

	    try {
	        Usuario usuario = usuarioRepositorio.getById(usuarioId);

	        List<Contacto> contactos = usuario.getContactos()
	                .stream()
	                .map(UsuarioContacto::getContacto)
	                .collect(Collectors.toList());

	        if (contactos.isEmpty()) {
	            response.setSuccess(false);
	            response.setMessage("El usuario no tiene contactos registrados.");
	            response.setStatusCode(HttpStatus.NOT_FOUND.value());
	        } else {
	            response.setSuccess(true);
	            response.setMessage("Contactos obtenidos exitosamente.");
	            response.setResult(contactos);
	            response.setStatusCode(HttpStatus.OK.value());
	        }
	    } catch (EntityNotFoundException e) {
	        response.setSuccess(false);
	        response.setMessage("No se encontró el usuario con el ID: " + usuarioId);
	        response.setStatusCode(HttpStatus.NOT_FOUND.value());
	    } catch (Exception e) {
	        response.setSuccess(false);
	        response.setMessage("Error al obtener los contactos: " + e.getMessage());
	        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	    }

	    return response;
	}

	//Método para guardar el contacto
	@PostMapping("/contactos/{usuarioId}")
	public ApiResponse<String> guardarContacto(@PathVariable Long usuarioId, @RequestBody Contacto contacto) {
	    ApiResponse<String> response = new ApiResponse<>();

	    try {
	    	// Verifica si el usuario existe
		    Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(usuarioId);
		    if (!usuarioOptional.isPresent()) {
		        response.setSuccess(false);
		        response.setMessage("No existe el usuario con el ID: " + usuarioId);
		        response.setStatusCode(HttpStatus.NOT_FOUND.value());
		        return response;
		    }

		    Usuario usuario = usuarioOptional.get();

		    // Verifica si el contacto ya está asociado al usuario
		    boolean contactoExistente = usuario.getContactos().stream()
		            .anyMatch(uc -> uc.getContacto().getTelefono().equals(contacto.getTelefono()));

		    if (contactoExistente) {
		        response.setSuccess(false);
		        response.setMessage("El número ya se encuentra registrado.");
		        response.setStatusCode(HttpStatus.CONFLICT.value());
		        return response;
		    } else {
	            // Guarda el contacto
	            contactoRepositorio.save(contacto);

	            // Crea la relación UsuarioContacto
	            UsuarioContacto usuarioContacto = new UsuarioContacto();
	            usuarioContacto.setUsuario(usuario);
	            usuarioContacto.setContacto(contacto);

	            // Guarda la relación UsuarioContacto
	            usuarioContactoRepositorio.save(usuarioContacto);

	            response.setSuccess(true);
	            response.setMessage("Contacto guardado exitosamente.");
	            response.setStatusCode(HttpStatus.OK.value());
	        }
	    } catch (Exception e) {
	        response.setSuccess(false);
	        response.setMessage("Error: " + e.getMessage());
	        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	    }

	    return response;
	}
	
	//Método para buscar un contacto por id
	@GetMapping("/contacto/{contactoId}")
	public ApiResponse<Optional<Contacto>> obtenerContactoPorId(@PathVariable Long contactoId) {
		ApiResponse<Optional<Contacto>> response = new ApiResponse<Optional<Contacto>>();
		
	    Optional<Contacto> contacto = contactoRepositorio.findById(contactoId);

	    if (contacto.isPresent()) {
	    	response.setSuccess(true);
            response.setMessage("Contacto encontrado");
            response.setResult(contacto);
            response.setStatusCode(HttpStatus.OK.value());
	    } else {
	    	response.setSuccess(false);
            response.setMessage("Contacto no encontrado");
            response.setResult(contacto);
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
	    }
	    
	    return response;
	}
	
	//Método para actualizar contacto
	@PutMapping("/contactos/{usuarioId}/{contactoId}")
	public ApiResponse<Contacto> actualizarContacto(@PathVariable Long usuarioId, @PathVariable Long contactoId, @RequestBody Contacto detallesContacto) {
	    ApiResponse<Contacto> response = new ApiResponse<>();

	    // Verifica si el usuario existe
	    Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(usuarioId);
	    if (!usuarioOptional.isPresent()) {
	        response.setSuccess(false);
	        response.setMessage("No existe el usuario con el ID: " + usuarioId);
	        response.setStatusCode(HttpStatus.NOT_FOUND.value());
	        return response;
	    }

	    Usuario usuario = usuarioOptional.get();

	    // Verifica si el contacto existe
	    Optional<Contacto> contactoOptional = contactoRepositorio.findById(contactoId);
	    if (!contactoOptional.isPresent()) {
	        response.setSuccess(false);
	        response.setMessage("No existe el contacto con el ID: " + contactoId);
	        response.setStatusCode(HttpStatus.NOT_FOUND.value());
	        return response;
	    }

	    Contacto contacto = contactoOptional.get();

	    // Verifica si hay cambios en los datos del contacto
	    if (detallesContacto.equals(contacto)) {
	        response.setSuccess(true);
	        response.setMessage("No se realizaron cambios en los datos del contacto.");
	        response.setStatusCode(HttpStatus.OK.value());
	        return response;
	    }

	    // Verifica si hay otro contacto con los mismos datos
	    boolean otroContactoConMismosDatos = usuario.getContactos().stream()
	            .anyMatch(uc -> {
	                Contacto otroContacto = uc.getContacto();
	                return otroContacto.getId() != contacto.getId() &&
	                        otroContacto.getNombre().equals(detallesContacto.getNombre()) &&
	                        otroContacto.getApellido().equals(detallesContacto.getApellido()) &&
	                        otroContacto.getTelefono().equals(detallesContacto.getTelefono());
	            });

	    if (otroContactoConMismosDatos) {
	        response.setSuccess(false);
	        response.setMessage("Ya hay otro contacto con esos datos.");
	        response.setStatusCode(HttpStatus.CONFLICT.value());
	        return response;
	    }

	    // Actualiza los datos del contacto
	    contacto.setNombre(detallesContacto.getNombre());
	    contacto.setApellido(detallesContacto.getApellido());
	    contacto.setTelefono(detallesContacto.getTelefono());

	    Contacto contactoActualizado = contactoRepositorio.save(contacto);

	    response.setSuccess(true);
	    response.setMessage("Contacto actualizado exitosamente.");
	    response.setResult(contactoActualizado);
	    response.setStatusCode(HttpStatus.OK.value());

	    return response;
	}
	
	//Método para eliminar un contacto
	@DeleteMapping("/contactos/{usuarioId}/{contactoId}")
	public ApiResponse<Map<String, Boolean>> eliminarContactoYRelacion(@PathVariable Long usuarioId, @PathVariable Long contactoId) {
	    ApiResponse<Map<String, Boolean>> response = new ApiResponse<>();

	    // Verifica si el usuario existe
	    Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(usuarioId);
	    if (!usuarioOptional.isPresent()) {
	        response.setSuccess(false);
	        response.setMessage("No existe el usuario con el ID: " + usuarioId);
	        response.setStatusCode(HttpStatus.NOT_FOUND.value());
	        return response;
	    }

	    Usuario usuario = usuarioOptional.get();

	    // Verifica si el contacto existe
	    Optional<Contacto> contactoOptional = contactoRepositorio.findById(contactoId);
	    if (!contactoOptional.isPresent()) {
	        response.setSuccess(false);
	        response.setMessage("No existe el contacto con el ID: " + contactoId);
	        response.setStatusCode(HttpStatus.NOT_FOUND.value());
	        return response;
	    }

	    Contacto contacto = contactoOptional.get();

	    // Verifica si el contacto está asociado al usuario
	    Optional<UsuarioContacto> usuarioContactoOptional = usuarioContactoRepositorio
	            .findByUsuarioAndContacto(usuario, contacto);

	    if (!usuarioContactoOptional.isPresent()) {
	        response.setSuccess(false);
	        response.setMessage("El contacto no está asociado al usuario.");
	        response.setStatusCode(HttpStatus.BAD_REQUEST.value()); 
	        return response;
	    }

	    // Elimina la relación usuario-contacto
	    usuarioContactoRepositorio.delete(usuarioContactoOptional.get());

	    // Elimina el contacto
	    contactoRepositorio.delete(contacto);

	    Map<String, Boolean> respuesta = new HashMap<>();
	    respuesta.put("eliminar", Boolean.TRUE);

	    response.setSuccess(true);
	    response.setMessage("Contacto eliminado exitosamente.");
	    response.setResult(respuesta);
	    response.setStatusCode(HttpStatus.OK.value());

	    return response;
	}

}
