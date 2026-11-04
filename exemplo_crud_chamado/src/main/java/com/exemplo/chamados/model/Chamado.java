package com.exemplo.chamados.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String endereco;
    private String tipo;
    private LocalDateTime dataHora;
    private String imagem;
    private String status = "Aberto";
    private String comentarioSabesp;

    public Chamado() {
        this.dataHora = LocalDateTime.now();
    }

    public Chamado(String endereco, String tipo, String imagem) {
        this.endereco = endereco;
        this.tipo = tipo;
        this.imagem = imagem;
        this.dataHora = LocalDateTime.now();
        this.status = "Aberto";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComentarioSabesp() {
        return comentarioSabesp;
    }

    public void setComentarioSabesp(String comentarioSabesp) {
        this.comentarioSabesp = comentarioSabesp;
    }
}
