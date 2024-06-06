package br.com.fiap.globalsolution.coralsafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.globalsolution.coralsafe.model.Usuario;


public interface UserRepository extends JpaRepository<Usuario, Long>{
    
}
