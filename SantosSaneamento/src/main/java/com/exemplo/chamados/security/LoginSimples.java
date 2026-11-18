package com.exemplo.chamados.security;

/**
 * Login bem simples, só para o trabalho: 3 contas fixas, sem banco e sem hash.
 *
 *   cidadao@santos.sp.gov.br  / cidadao123     -> Cidadão
 *   operador@sabesp.com.br    / sabesp123      -> Sabesp
 *   gestor@santos.sp.gov.br   / prefeitura123  -> Prefeitura
 */
public class LoginSimples {

    private LoginSimples() {
    }

    /** Devolve o usuário se e-mail e senha batem com uma das 3 contas; senão, null. */
    public static Usuario autenticar(String email, String senha) {
        if (email == null || senha == null) {
            return null;
        }
        email = email.trim().toLowerCase();

        if (email.equals("cidadao@santos.sp.gov.br") && senha.equals("cidadao123")) {
            return new Usuario("cidadao", "Marina Oliveira", "Cidadã", "MO");
        } else if (email.equals("operador@sabesp.com.br") && senha.equals("sabesp123")) {
            return new Usuario("sabesp", "Marcos Silva", "Operador Sabesp", "MS");
        } else if (email.equals("gestor@santos.sp.gov.br") && senha.equals("prefeitura123")) {
            return new Usuario("prefeitura", "Fernanda Costa", "Gestora Municipal", "FC");
        }
        return null;
    }
}
