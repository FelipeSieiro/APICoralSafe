package br.com.fiap.globalsolution.coralsafe.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.hateoas.EntityModel;

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
public class Campanha{

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String descricaoCampanha;

    private BigDecimal valorMeta;

    private LocalDate dataPublicacao;

    @ManyToOne
    private Ong ong;

    public EntityModel<Campanha> toEntityModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(CampanhaController.class).show(id)).withSelfRel(),
            linkTo(methodOn(CampanhaController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(CampanhaController.class).update(id, this)).withRel("update"),
            linkTo(methodOn(CampanhaController.class).index()).withRel("all")
        );
    }
}
