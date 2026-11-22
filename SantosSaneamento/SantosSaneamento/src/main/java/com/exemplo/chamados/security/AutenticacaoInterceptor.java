package com.exemplo.chamados.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Barra quem não fez login e quem tenta abrir página de outro perfil.
 *   cidadão    -> registrar e acompanhar os próprios chamados
 *   sabesp     -> fila, detalhes, alterar status e comentar
 *   prefeitura -> painel, todas as ocorrências e filtros
 */
public class AutenticacaoInterceptor implements HandlerInterceptor {

    public static final String SESSAO_USUARIO = "usuarioLogado";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String caminho = request.getRequestURI().substring(request.getContextPath().length());

        // páginas públicas
        if (caminho.equals("/") || caminho.equals("/login") || caminho.equals("/entrar") || caminho.equals("/sair")) {
            return true;
        }

        HttpSession sessao = request.getSession(false);
        Usuario usuario = sessao == null ? null : (Usuario) sessao.getAttribute(SESSAO_USUARIO);

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        if (!podeAcessar(usuario.getPerfil(), caminho)) {
            response.sendRedirect(request.getContextPath() + "/login?acesso");
            return false;
        }
        return true;
    }

    private boolean podeAcessar(String perfil, String caminho) {
        // só o cidadão registra e edita chamados
        if (caminho.startsWith("/cidadao") || caminho.equals("/chamados/novo")
                || caminho.equals("/chamados/salvar") || caminho.startsWith("/chamados/editar")) {
            return perfil.equals("cidadao");
        }
        // só a Sabesp atualiza status e comentário
        if (caminho.startsWith("/sabesp") || caminho.startsWith("/chamados/atualizar")) {
            return perfil.equals("sabesp");
        }
        // só a prefeitura vê o painel gerencial
        if (caminho.startsWith("/prefeitura") || caminho.startsWith("/chamados/excluir")) {
            return perfil.equals("prefeitura");
        }
        // lista geral: Sabesp e prefeitura
        if (caminho.equals("/chamados")) {
            return perfil.equals("sabesp") || perfil.equals("prefeitura");
        }
        // detalhes, fotos (/uploads) e o resto: qualquer perfil logado
        return true;
    }
}
