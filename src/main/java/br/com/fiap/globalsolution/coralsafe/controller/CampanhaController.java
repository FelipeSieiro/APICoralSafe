package br.com.fiap.globalsolution.coralsafe.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.globalsolution.coralsafe.model.Campanha;
import br.com.fiap.globalsolution.coralsafe.repository.CampanhaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("campanha")
@CacheConfig(cacheNames = "campanhas")
@Slf4j
@Tag(name = "categorias", description = "Campanhas para serem apoiadas")
public class CampanhaController {

    @Autowired
    CampanhaRepository repository;

    @Autowired
    PagedResourcesAssembler<Campanha> pageAssembler;

    @GetMapping
    @Cacheable
    @Operation(
        summary = "Listar Campanhas",
        description = "Retorna um array com todas as campanhas cadastradas."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de campanhas retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public List<EntityModel<Campanha>> index() {
        List<Campanha> campanhas = repository.findAll();
        return campanhas.stream()
                        .map(Campanha::toEntityModel)
                        .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @CacheEvict(allEntries = true)
    @Operation(
        summary = "Cadastrar Campanha",
        description = "Cadastra uma nova campanha para ONGs logada com os dados enviados no corpo da requisição."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Campanha cadastrada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Validação falhou. Verifique as regras para o corpo da requisição", useReturnTypeSchema = false)
    })
    public EntityModel<Campanha> create(@RequestBody Campanha campanha) { 
        log.info("cadastrando campanha {} ", campanha);
        Campanha savedCampanha = repository.save(campanha);
        return savedCampanha.toEntityModel();
    }

    @GetMapping("{id}")
    @Operation(
        summary = "Buscar Campanha",
        description = "Retorna uma campanha pelo ID informado."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Campanha encontrada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Campanha não encontrada")
    })
    public EntityModel<Campanha> show(@PathVariable Long id){
        var campanha = repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("campanha não encontrada")
        );

        return campanha.toEntityModel();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @CacheEvict(allEntries = true)
    @Operation(
        summary = "Deletar Campanha",
        description = "Deleta uma campanha pelo ID informado."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Campanha deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Campanha não encontrada")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id){
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("campanha não encontrada")
        );

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @CacheEvict(allEntries = true)
    @Operation(
        summary = "Atualizar Campanha",
        description = "Atualiza uma campanha pelo ID informado com os dados enviados no corpo da requisição."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Campanha atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Campanha não encontrada"),
        @ApiResponse(responseCode = "400", description = "Validação falhou. Verifique as regras para o corpo da requisição", useReturnTypeSchema = false)
    })
    public EntityModel<Campanha> update(@PathVariable Long id, @RequestBody Campanha campanha) {
        log.info("atualizando campanha com id {} para {}", id, campanha);

        verificarSeExisteCampanha(id);
        campanha.setId(id);
        Campanha updatedCampanha = repository.save(campanha);
        return updatedCampanha.toEntityModel();
    }

    private void verificarSeExisteCampanha(Long id) {
        repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Não existe campanha com o id informado. Consulte lista em /campanha"));
    }
}
