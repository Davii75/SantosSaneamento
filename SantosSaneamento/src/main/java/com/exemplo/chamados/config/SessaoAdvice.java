package com.exemplo.chamados.config;

import com.exemplo.chamados.security.AutenticacaoInterceptor;
import com.exemplo.chamados.security.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Coloca o usuário logado em todas as telas (a barra lateral usa isso). */
@ControllerAdvice
public class SessaoAdvice {

    @ModelAttribute
    public void usuarioLogado(HttpSession sessao, Model model) {
        Usuario usuario = (Usuario) sessao.getAttribute(AutenticacaoInterceptor.SESSAO_USUARIO);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("perfil", usuario.getPerfil());
        }
    }
}
