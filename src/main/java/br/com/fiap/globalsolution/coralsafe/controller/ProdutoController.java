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

import br.com.fiap.globalsolution.coralsafe.model.Produto;
import br.com.fiap.globalsolution.coralsafe.repository.ProdutosRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("produtos")
@Slf4j
@Tag(name = "produtos", description = "Gerenciamento de produtos")
public class ProdutoController {

    @Autowired
    ProdutosRepository repository;


    @GetMapping
    @Operation(
        summary = "Listar Produtos",
        description = "Retorna um array com todos os produtos cadastrados."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public List<EntityModel<Produto>> index() {
        List<Produto> produtos = repository.findAll();
        return produtos.stream()
                       .map(Produto::toEntityModel)
                       .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(
        summary = "Cadastrar Produto",
        description = "Cadastra um novo produto com os dados enviados no corpo da requisição."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Validação falhou. Verifique as regras para o corpo da requisição", useReturnTypeSchema = false)
    })
     public ResponseEntity<Produto> create(@RequestBody @Valid Produto produto) {
        repository.save(produto);

        return ResponseEntity
                    .created(produto.toEntityModel().getRequiredLink("self").toUri())
                    .body(produto);
    }

    @GetMapping("{id}")
    @Operation(
        summary = "Buscar Produto",
        description = "Retorna um produto pelo ID informado."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<EntityModel<Produto>> show(@PathVariable Long id) {
        log.info("buscando produto por id {}", id);

        return repository
                .findById(id)
                .map(produto -> ResponseEntity.ok(produto.toEntityModel()))
                .orElse(ResponseEntity.notFound().build());
    }
}
