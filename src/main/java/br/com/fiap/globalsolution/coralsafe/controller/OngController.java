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

import br.com.fiap.globalsolution.coralsafe.model.Ong;
import br.com.fiap.globalsolution.coralsafe.repository.OngRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("ong")
@Slf4j
@Tag(name = "ongs", description = "Organizações não governamentais")
public class OngController {

    @Autowired
    OngRepository repository;

    @GetMapping
    @Operation(
        summary = "Listar ONGs",
        description = "Retorna um array com todas as ONGs cadastradas."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de ONGs retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public List<EntityModel<Ong>> index() {
        List<Ong> ongs = repository.findAll();
        return ongs.stream()
                   .map(Ong::toEntityModel)
                   .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(
        summary = "Cadastrar ONG",
        description = "Cadastra uma nova ONG com os dados enviados no corpo da requisição."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "ONG cadastrada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Validação falhou. Verifique as regras para o corpo da requisição", useReturnTypeSchema = false)
    })
    public EntityModel<Ong> create(@RequestBody Ong ong) { 
        log.info("cadastrando ong {} ", ong);
        Ong savedOng = repository.save(ong);
        return savedOng.toEntityModel();
    }

    @GetMapping("{id}")
    @Operation(
        summary = "Buscar ONG",
        description = "Retorna uma ONG pelo ID informado."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ONG encontrada com sucesso"),
        @ApiResponse(responseCode = "404", description = "ONG não encontrada")
    })
    public ResponseEntity<EntityModel<Ong>> show(@PathVariable Long id) {
        log.info("buscando ong por id {}", id);

        return repository
                .findById(id)
                .map(ong -> ResponseEntity.ok(ong.toEntityModel()))
                .orElse(ResponseEntity.notFound().build());
    }
}
                                     