package com.exemplo.chamados.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Chamado {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String endereco;
    private String bairro;
    private String referencia;
    private String tipo;
    @Column(length=1000) private String descricao;
    private String solicitante;
    private LocalDateTime dataHora;
    private String imagem;
    private String status = "Novo";
    @Column(length=1500) private String comentarioSabesp;
    public Chamado(){ this.dataHora=LocalDateTime.now(); }
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getEndereco(){return endereco;} public void setEndereco(String v){endereco=v;}
    public String getBairro(){return bairro;} public void setBairro(String v){bairro=v;}
    public String getReferencia(){return referencia;} public void setReferencia(String v){referencia=v;}
    public String getTipo(){return tipo;} public void setTipo(String v){tipo=v;}
    public String getDescricao(){return descricao;} public void setDescricao(String v){descricao=v;}
    public String getSolicitante(){return solicitante;} public void setSolicitante(String v){solicitante=v;}
    public LocalDateTime getDataHora(){return dataHora;} public void setDataHora(LocalDateTime v){dataHora=v;}
    public String getImagem(){return imagem;} public void setImagem(String v){imagem=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public String getComentarioSabesp(){return comentarioSabesp;} public void setComentarioSabesp(String v){comentarioSabesp=v;}
}
