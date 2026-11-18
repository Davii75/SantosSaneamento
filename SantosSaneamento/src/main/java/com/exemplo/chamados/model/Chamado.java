package com.exemplo.chamados.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chamados")
public class Chamado {

    // Opções usadas nos formulários e filtros
    public static final List<String> BAIRROS =
            List.of("Boqueirão", "Centro", "Gonzaga", "Aparecida", "Vila Belmiro");
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
