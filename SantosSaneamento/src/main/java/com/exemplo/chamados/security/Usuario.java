package com.exemplo.chamados.security;

import java.io.Serializable;

/** Usuário logado (guardado na sessão). Só existem 3 no sistema. */
public class Usuario implements Serializable {

    private final String perfil;   // "cidadao", "sabesp" ou "prefeitura"
    private final String nome;
    private final String cargo;
    private final String iniciais;

    public Usuario(String perfil, String nome, String cargo, String iniciais) {
        this.perfil = perfil;
        this.nome = nome;
        this.cargo = cargo;
        this.iniciais = iniciais;
    }

    public String getPerfil() { return perfil; }
    public String getNome() { return nome; }
    public String getCargo() { return cargo; }
    public String getIniciais() { return iniciais; }

    /** Página inicial de cada perfil depois do login. */
    public String getPaginaInicial() {
        if ("cidadao".equals(perfil)) return "/chamados/novo";
        if ("prefeitura".equals(perfil)) return "/prefeitura/painel";
        return "/sabesp/chamados";
    }
}
