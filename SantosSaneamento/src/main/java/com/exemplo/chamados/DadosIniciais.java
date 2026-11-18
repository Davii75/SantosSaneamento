package com.exemplo.chamados;

import com.exemplo.chamados.model.Chamado;
import com.exemplo.chamados.repository.ChamadoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/** Cria alguns chamados de exemplo quando o banco está vazio, para as telas não ficarem vazias. */
@Component
public class DadosIniciais implements CommandLineRunner {

    private final ChamadoRepository repository;

    public DadosIniciais(ChamadoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        criar(new Chamado("Av. Conselheiro Nébias, 542", "Centro", "Vazamento de esgoto", "Marina Oliveira",
                "Esgoto vazando na calçada há três dias."), "Novo", 1, null);
        criar(new Chamado("Rua Euclides da Cunha, 120", "Gonzaga", "Bueiro transbordando", "Marina Oliveira",
                "Bueiro transborda sempre que chove."), "Em atendimento", 4, "Equipe enviada ao local.");
        criar(new Chamado("Av. Bernardino de Campos, 88", "Boqueirão", "Mau cheiro", "Marina Oliveira",
                "Cheiro forte vindo da galeria."), "Resolvido", 9, "Limpeza da galeria concluída.");
        criar(new Chamado("Rua Oswaldo Cruz, 301", "Boqueirão", "Rede obstruída", "João Pereira",
                "Rede entupida em frente ao número 301."), "Em análise", 2, null);
        criar(new Chamado("Rua Pará, 45", "Aparecida", "Vazamento de esgoto", "Ana Souza",
                "Vazamento próximo à escola."), "Novo", 0, null);
    }

    private void criar(Chamado chamado, String status, int diasAtras, String comentario) {
        chamado.setStatus(status);
        chamado.setComentarioSabesp(comentario);
        chamado.setDataHora(LocalDateTime.now().minusDays(diasAtras));
        repository.save(chamado);
    }
}
