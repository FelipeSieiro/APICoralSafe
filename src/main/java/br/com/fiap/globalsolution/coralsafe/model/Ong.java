package br.com.fiap.globalsolution.coralsafe.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;

import org.springframework.hateoas.EntityModel;

import br.com.fiap.globalsolution.coralsafe.controller.OngController;
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
public class Ong{
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    private String descricao;

    private LocalDate dataFundacao;

    private String endereco;

    private String cidade;

    private String estado;

    private String pais;

    private String telefone;

    private String email;

    private String cnpj;

    private int numeroVoluntarios;

    public EntityModel<Ong> toEntityModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(OngController.class).show(id)).withSelfRel(),
            linkTo(methodOn(OngController.class).index()).withRel("all"),
            linkTo(methodOn(OngController.class).create(this)).withRel("create")
        );
    }
}
