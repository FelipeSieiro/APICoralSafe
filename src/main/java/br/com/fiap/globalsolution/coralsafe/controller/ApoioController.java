package br.com.fiap.globalsolution.coralsafe.controller;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.globalsolution.coralsafe.model.Apoio;
import br.com.fiap.globalsolution.coralsafe.repository.ApoioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("apoio")
@Slf4j
@Tag(name = "apoios", description = "Apoios para campanhas")
public class ApoioController {

    @Autowired
    ApoioRepository repository;

    @GetMapping
    @Operation(
        summary = "Listar Apoios",
        description = "Retorna um array com todos os apoios cadastrados."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de apoios retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public List<EntityModel<Apoio>> index() {
        List<Apoio> apoios = repository.findAll();
        return apoios.stream()
                     .map(Apoio::toEntityModel)
                     .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(
        summary = "Cadastrar Apoio",
        description = "Cadastra um novo apoio com os dados enviados no corpo da requisição."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Apoio cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Validação falhou. Verifique as regras para o corpo da requisição", useReturnTypeSchema = false)
    })
     public ResponseEntity<Apoio> create(@RequestBody @Valid Apoio apoio) {
        repository.save(apoio);

        return ResponseEntity
                    .created(apoio.toEntityModel().getRequiredLink("self").toUri())
                    .body(apoio);
    }

    @GetMapping("{id}")
    @Operation(
        summary = "Buscar Apoio",
        description = "Retorna um apoio pelo ID informado."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Apoio encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Apoio não encontrado")
    })
    public ResponseEntity<EntityModel<Apoio>> show(@PathVariable Long id) {
        log.info("buscando apoio por id {}", id);

        return repository
                .findById(id)
                .map(apoio -> ResponseEntity.ok(apoio.toEntityModel()))
                .orElse(ResponseEntity.notFound().build());
    }
}
