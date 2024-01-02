package com.gestion.contactos.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.contactos.modelo.Contacto;
import com.gestion.contactos.modelo.Usuario;
import com.gestion.contactos.modelo.UsuarioContacto;

@Repository
public interface UsuarioContactoRepositorio extends JpaRepository<UsuarioContacto, Long>{
    Optional<UsuarioContacto> findByUsuarioAndContacto(Usuario usuario, Contacto contacto);
}
