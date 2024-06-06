package br.com.fiap.globalsolution.coralsafe.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.fiap.globalsolution.coralsafe.model.Usuario;
import br.com.fiap.globalsolution.coralsafe.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("user")
@Slf4j
@Tag(name = "users", description = "Gerenciamento de usuários")
public class UserController {
    
    @Autowired
    UserRepository repository;

    @GetMapping
    @Operation(
        summary = "Listar Usuários",
        description = "Retorna um array com todos os usuários cadastrados."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public List<EntityModel<Usuario>> index() {
        List<Usuario> usuarios = repository.findAll();
        return usuarios.stream()
                        .map(Usuario::toEntityModel)
                        .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(
        summary = "Cadastrar Usuário",
        description = "Cadastra um novo usuário com os dados enviados no corpo da requisição."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Validação falhou. Verifique as regras para o corpo da requisição", useReturnTypeSchema = false)
    })
     public ResponseEntity<Usuario> create(@RequestBody @Valid Usuario usuario) {
        repository.save(usuario);

        return ResponseEntity
                    .created(usuario.toEntityModel().getRequiredLink("self").toUri())
                    .body(usuario);
    }

    @GetMapping("{id}")
    @Operation(
        summary = "Buscar Usuário",
        description = "Retorna um usuário pelo ID informado."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public EntityModel<Usuario> show(@PathVariable Long id) {
        log.info("buscando usuário por id {}", id);
        Usuario usuario = repository.findById(id)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        return usuario.toEntityModel();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Deletar Usuário",
        description = "Deleta um usuário pelo ID informado."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        log.info("apagando usuário");

        verificarSeExisteUser(id);
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @Operation(
        summary = "Atualizar Usuário",
        description = "Atualiza um usuário pelo ID informado com os dados enviados no corpo da requisição."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "400", description = "Validação falhou. Verifique as regras para o corpo da requisição", useReturnTypeSchema = false)
    })
    public EntityModel<Usuario> update(@PathVariable Long id, @RequestBody Usuario user) {
        log.info("atualizando usuário com id {} para {}", id, user);

        verificarSeExisteUser(id);
        user.setId(id);
        Usuario updatedUser = repository.save(user);
        return updatedUser.toEntityModel();
    }

    private void verificarSeExisteUser(Long id) {
        repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Não existe usuário com o id informado. Consulte lista em /user"));
    }
}
