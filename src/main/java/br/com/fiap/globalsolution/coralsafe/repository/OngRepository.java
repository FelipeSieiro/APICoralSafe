package br.com.fiap.globalsolution.coralsafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.globalsolution.coralsafe.model.Ong;

public interface OngRepository extends JpaRepository<Ong, Long>{
    
}
