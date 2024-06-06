package br.com.fiap.globalsolution.coralsafe.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.hateoas.EntityModel;

import br.com.fiap.globalsolution.coralsafe.controller.ApoioController;
import br.com.fiap.globalsolution.coralsafe.controller.CampanhaController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Apoio{
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal valorApoiado;

    private LocalDate dataApoio;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Campanha campanha;

    public EntityModel<Apoio> toEntityModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(ApoioController.class).show(id)).withSelfRel(),
            linkTo(methodOn(ApoioController.class).index()).withRel("all"),
            linkTo(methodOn(CampanhaController.class).show(campanha.getId())).withRel("campanha")
        );
    }
}
