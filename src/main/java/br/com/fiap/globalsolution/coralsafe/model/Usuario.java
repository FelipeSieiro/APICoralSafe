package br.com.fiap.globalsolution.coralsafe.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.hateoas.EntityModel;

import br.com.fiap.globalsolution.coralsafe.controller.UserController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String nome;
    private String email;
    private String senha;
    private String telefoneContato;
    
    public EntityModel<Usuario> toEntityModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(UserController.class).show(id)).withSelfRel(),
            linkTo(methodOn(UserController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(UserController.class).update(id, this)).withRel("update"),
            linkTo(methodOn(UserController.class).index()).withRel("all")
        );
    }
}
