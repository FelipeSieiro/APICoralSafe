package br.com.fiap.globalsolution.coralsafe.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;

import br.com.fiap.globalsolution.coralsafe.controller.ProdutoController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Produto{
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nomeProduto;

    private String descricaoProduto;
    
    private int qtdPontosResgate;

    public EntityModel<Produto> toEntityModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(ProdutoController.class).show(id)).withSelfRel(),
            linkTo(methodOn(ProdutoController.class).index()).withRel("all"),
            linkTo(methodOn(ProdutoController.class).create(this)).withRel("create")
        );
    }
}
