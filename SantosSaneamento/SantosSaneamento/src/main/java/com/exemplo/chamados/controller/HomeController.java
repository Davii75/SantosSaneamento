package com.exemplo.chamados.controller;

import com.exemplo.chamados.model.Chamado;
import com.exemplo.chamados.security.AutenticacaoInterceptor;
import com.exemplo.chamados.security.LoginSimples;
import com.exemplo.chamados.security.Usuario;
import com.exemplo.chamados.service.ChamadoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final ChamadoService service;

    public HomeController(ChamadoService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home(HttpSession sessao) {
        Usuario usuario = (Usuario) sessao.getAttribute(AutenticacaoInterceptor.SESSAO_USUARIO);
        return usuario == null ? "redirect:/login" : "redirect:" + usuario.getPaginaInicial();
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /** Confere e-mail e senha (3 contas fixas) e manda cada perfil para a sua tela. */
    @PostMapping("/entrar")
    public String entrar(@RequestParam String email, @RequestParam String senha, HttpSession sessao) {
        Usuario usuario = LoginSimples.autenticar(email, senha);
        if (usuario == null) {
            return "redirect:/login?erro";
        }
        sessao.setAttribute(AutenticacaoInterceptor.SESSAO_USUARIO, usuario);
        return "redirect:" + usuario.getPaginaInicial();
    }

    @GetMapping("/sair")
    public String sair(HttpSession sessao) {
        sessao.invalidate();
        return "redirect:/login";
    }

    // ---------- Cidadão ----------

    @GetMapping("/cidadao/chamados")
    public String meusChamados(HttpSession sessao, Model model) {
        Usuario usuario = (Usuario) sessao.getAttribute(AutenticacaoInterceptor.SESSAO_USUARIO);
        List<Chamado> chamados = service.listarDoSolicitante(usuario.getNome());
        long resolvidos = service.contarPorStatus(chamados, "Resolvido");

        model.addAttribute("chamados", chamados);
        model.addAttribute("total", chamados.size());
        model.addAttribute("andamento", chamados.size() - resolvidos);
        model.addAttribute("resolvidos", resolvidos);
        return "chamados/acompanhamento";
    }

    // ---------- Sabesp ----------

    @GetMapping("/sabesp/chamados")
    public String filaSabesp() {
        return "redirect:/chamados";
    }

    // ---------- Prefeitura ----------

    @GetMapping("/prefeitura/chamados")
    public String ocorrenciasPrefeitura() {
        return "redirect:/chamados";
    }

    @GetMapping("/prefeitura/painel")
    public String painel(Model model) {
        List<Chamado> chamados = service.listarTodos();
        long total = chamados.size();
        long resolvidos = service.contarPorStatus(chamados, "Resolvido");

        model.addAttribute("total", total);
        model.addAttribute("novos", service.contarPorStatus(chamados, "Novo"));
        model.addAttribute("atendimento", service.contarPorStatus(chamados, "Em atendimento"));
        model.addAttribute("resolvidos", resolvidos);
        model.addAttribute("taxa", total == 0 ? 0 : Math.round(resolvidos * 100.0 / total));
        model.addAttribute("chamados", chamados);
        return "chamados/painel";
    }
}
