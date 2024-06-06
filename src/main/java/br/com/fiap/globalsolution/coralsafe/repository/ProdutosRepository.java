package br.com.fiap.globalsolution.coralsafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.globalsolution.coralsafe.model.Produto;

public interface ProdutosRepository  extends JpaRepository<Produto, Long>{
    
}
