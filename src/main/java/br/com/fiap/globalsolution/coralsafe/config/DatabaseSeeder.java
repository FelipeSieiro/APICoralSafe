package br.com.fiap.globalsolution.coralsafe.config;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.globalsolution.coralsafe.model.Apoio;
import br.com.fiap.globalsolution.coralsafe.model.Campanha;
import br.com.fiap.globalsolution.coralsafe.model.Ong;
import br.com.fiap.globalsolution.coralsafe.model.Produto;
import br.com.fiap.globalsolution.coralsafe.model.Usuario;
import br.com.fiap.globalsolution.coralsafe.repository.ApoioRepository;
import br.com.fiap.globalsolution.coralsafe.repository.CampanhaRepository;
import br.com.fiap.globalsolution.coralsafe.repository.OngRepository;
import br.com.fiap.globalsolution.coralsafe.repository.ProdutosRepository;
import br.com.fiap.globalsolution.coralsafe.repository.UserRepository;



@Configuration
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    UserRepository usuarioRepository;

    @Autowired
    ApoioRepository apoioRepository;

    @Autowired
    ProdutosRepository produtoRepository;

    @Autowired
    OngRepository ongRepository;

    @Autowired
    CampanhaRepository campanhaRepository;

    @Override
    public void run(String... args) throws Exception {
        ongRepository.saveAll(
            List.of(
                Ong.builder()
                    .id(1L)
                    .nome("CoralSafe")
                    .descricao("ONG dedicada à proteção dos corais")
                    .dataFundacao(LocalDate.of(2020, 1, 1))
                    .endereco("Rua dos Corais, 123")
                    .cidade("Recife")
                    .estado("PE")
                    .pais("Brasil")
                    .telefone("123456789")
                    .email("contato@coralsafe.org")
                    .cnpj("12345678000100")
                    .numeroVoluntarios(50)
                    .build()
            )
        );

        campanhaRepository.saveAll(
            List.of(
                Campanha.builder()
                    .id(1L)
                    .descricaoCampanha("Proteção dos Corais")
                    .valorMeta(new BigDecimal("100000.00"))
                    .dataPublicacao(LocalDate.of(2023, 1, 1))
                    .ong(ongRepository.findById(1L).get())
                    .build()
            )
        );

        usuarioRepository.saveAll(
            List.of(
                Usuario.builder()
                    .id(1L)
                    .nome("João da Silva")
                    .email("joao.silva@example.com")
                    .senha("12345")
                    .telefoneContato("987654321")
                    .build()
            )
        );

        apoioRepository.saveAll(
            List.of(
                Apoio.builder()
                    .id(1L)
                    .valorApoiado(new BigDecimal("100.00"))
                    .dataApoio(LocalDate.now())
                    .usuario(usuarioRepository.findById(1L).get())
                    .campanha(campanhaRepository.findById(1L).get())
                    .build(),
                Apoio.builder()
                    .id(2L)
                    .valorApoiado(new BigDecimal("150.00"))
                    .dataApoio(LocalDate.now().minusWeeks(1))
                    .usuario(usuarioRepository.findById(1L).get())
                    .campanha(campanhaRepository.findById(1L).get())
                    .build()
            )
        );

        produtoRepository.saveAll(
            List.of(
                Produto.builder()
                    .id(1L)
                    .nomeProduto("Camiseta CoralSafe")
                    .descricaoProduto("Camiseta oficial da ONG CoralSafe")
                    .qtdPontosResgate(50)
                    .build(),
                Produto.builder()
                    .id(2L)
                    .nomeProduto("Caneca CoralSafe")
                    .descricaoProduto("Caneca oficial da ONG CoralSafe")
                    .qtdPontosResgate(30)
                    .build(),
                Produto.builder()
                    .id(3L)
                    .nomeProduto("Garrafa Reutilizável")
                    .descricaoProduto("Garrafa de água reutilizável")
                    .qtdPontosResgate(40)
                    .build(),
                Produto.builder()
                    .id(4L)
                    .nomeProduto("Sacola Ecológica")
                    .descricaoProduto("Sacola de compras ecológica")
                    .qtdPontosResgate(20)
                    .build(),
                Produto.builder()
                    .id(5L)
                    .nomeProduto("Kit Talheres de Bambu")
                    .descricaoProduto("Kit de talheres sustentáveis feitos de bambu")
                    .qtdPontosResgate(35)
                    .build(),
                Produto.builder()
                    .id(6L)
                    .nomeProduto("Bloco de Notas Reciclado")
                    .descricaoProduto("Bloco de notas feito com papel reciclado")
                    .qtdPontosResgate(15)
                    .build(),
                Produto.builder()
                    .id(7L)
                    .nomeProduto("Porta-copos de Cortiça")
                    .descricaoProduto("Porta-copos feitos de cortiça natural")
                    .qtdPontosResgate(25)
                    .build()
            )
        );
    }
}

