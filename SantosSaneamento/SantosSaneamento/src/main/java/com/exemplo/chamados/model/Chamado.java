package com.exemplo.chamados.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "chamados")
public class Chamado {

    // Opções usadas nos formulários e filtros
    /**
     * Bairros de Santos agrupados por região administrativa.
     * Fonte: Decreto Municipal nº 9.653/2022 (Prefeituras Regionais de Santos).
     */
    public static final Map<String, List<String>> BAIRROS_POR_REGIAO = criarBairrosPorRegiao();

    /** Lista simples com todos os bairros (usada para validar o que chega do formulário). */
    public static final List<String> BAIRROS = BAIRROS_POR_REGIAO.values().stream()
            .flatMap(List::stream)
            .toList();

    private static Map<String, List<String>> criarBairrosPorRegiao() {
        Map<String, List<String>> regioes = new LinkedHashMap<>();
        regioes.put("Orla / Intermediária", List.of(
                "Aparecida", "Boqueirão", "Campo Grande", "Embaré", "Encruzilhada", "Estuário",
                "Gonzaga", "Jabaquara", "José Menino", "Macuco", "Marapé", "Outeirinhos", "Pompéia",
                "Ponta da Praia", "Porto Macuco", "Porto Ponta da Praia", "Vila Belmiro", "Vila Hayden"));
        regioes.put("Centro Histórico", List.of(
                "Centro", "Chinês", "Paquetá", "Porto Paquetá", "Porto Valongo", "Valongo",
                "Vila Mathias", "Vila Nova"));
        regioes.put("Zona Noroeste", List.of(
                "Alemoa", "Areia Branca", "Bom Retiro", "Caneleira", "Castelo", "Chico de Paula",
                "Ilhéu Alto", "Piratininga", "Porto Alemoa", "Porto Saboó", "Rádio Clube", "Saboó",
                "Santa Maria", "São Jorge", "São Manoel", "Vila Haddad"));
        regioes.put("Morros", List.of(
                "Morro Cachoeira", "Morro Caneleira", "Morro Chico de Paula", "Morro Embaré",
                "Morro Fontana", "Morro Jabaquara", "Morro José Menino", "Morro Marapé",
                "Morro Monte Serrat", "Morro Nova Cintra", "Morro Pacheco", "Morro Penha",
                "Morro Saboó", "Morro Santa Maria", "Morro Santa Terezinha", "Morro São Bento",
                "Vila Progresso"));
        regioes.put("Área Continental", List.of(
                "Bagres", "Barnabé", "Cabuçú-Caetê", "Caruara", "Guarapá", "Iriri", "Monte Cabrão",
                "Nossa Senhora das Neves", "Piaçaguera", "Quilombo", "Trindade"));
        return Collections.unmodifiableMap(regioes);
    }
    public static final List<String> TIPOS =
            List.of("Vazamento de esgoto", "Bueiro transbordando", "Rede obstruída", "Mau cheiro", "Outros");
    public static final List<String> STATUS =
            List.of("Novo", "Em análise", "Em atendimento", "Resolvido");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O endereço é obrigatório.")
    @Size(max = 150, message = "O endereço deve ter no máximo 150 caracteres.")
    @Column(nullable = false, length = 150)
    private String endereco;

    @NotBlank(message = "Selecione o bairro.")
    @Column(nullable = false, length = 80)
    private String bairro;

    @Size(max = 150, message = "O ponto de referência deve ter no máximo 150 caracteres.")
    @Column(length = 150)
    private String referencia;

    @NotBlank(message = "Selecione o tipo de problema.")
    @Column(nullable = false, length = 80)
    private String tipo;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
    @Column(length = 500)
    private String descricao;

    @Column(length = 100)
    private String solicitante;

    @NotNull(message = "Informe a data e a hora.")
    @Column(nullable = false)
    private LocalDateTime dataHora;

    // nome do arquivo da foto (salvo na pasta de uploads)
    @Column(length = 100)
    private String imagem;

    @Column(nullable = false, length = 30)
    private String status = "Novo";

    @Column(length = 1500)
    private String comentarioSabesp;

    public Chamado() {
        this.dataHora = LocalDateTime.now();
    }

    public Chamado(String endereco, String bairro, String tipo, String solicitante, String descricao) {
        this();
        this.endereco = endereco;
        this.bairro = bairro;
        this.tipo = tipo;
        this.solicitante = solicitante;
        this.descricao = descricao;
    }

    /** O cidadão só pode editar enquanto a Sabesp ainda não começou a analisar. */
    public boolean podeSerEditado() {
        return "Novo".equals(status);
    }

    public boolean estaResolvido() {
        return "Resolvido".equals(status);
    }

    /** Classe CSS do selo de status (usada nas telas). */
    public String getStatusClasse() {
        if ("Em análise".equals(status)) return "status-em-analise";
        if ("Em atendimento".equals(status)) return "status-em-atendimento";
        if ("Resolvido".equals(status)) return "status-resolvido";
        return "status-novo";
    }

    public Long getId() { return id; }
    public String getEndereco() { return endereco; }
    public String getBairro() { return bairro; }
    public String getReferencia() { return referencia; }
    public String getTipo() { return tipo; }
    public String getDescricao() { return descricao; }
    public String getSolicitante() { return solicitante; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getImagem() { return imagem; }
    public String getStatus() { return status; }
    public String getComentarioSabesp() { return comentarioSabesp; }

    public void setId(Long id) { this.id = id; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public void setImagem(String imagem) { this.imagem = imagem; }
    public void setStatus(String status) { this.status = status; }
    public void setComentarioSabesp(String comentarioSabesp) { this.comentarioSabesp = comentarioSabesp; }
}
