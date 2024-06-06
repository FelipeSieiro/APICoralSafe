package br.com.fiap.globalsolution.coralsafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.globalsolution.coralsafe.model.Campanha;

public interface CampanhaRepository extends JpaRepository<Campanha, Long>{
    
}
