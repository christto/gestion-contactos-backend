package com.gestion.contactos.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import com.gestion.contactos.modelo.Contacto;

@Repository
public interface ContactoRepositorio extends JpaRepository<Contacto, Long>{
	
	Optional<Contacto> findById(Long contactoId);
	
	List<Contacto> findByNombreContaining(String nombre);
}
