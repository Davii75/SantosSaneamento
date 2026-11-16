package com.exemplo.chamados.controller;

import com.exemplo.chamados.service.ChamadoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/entrar")
    public String entrar(@RequestParam String perfil) {
        if ("cidadao".equals(perfil)) {
            return "redirect:/cidadao/novo";
        }
        if ("prefeitura".equals(perfil)) {
            return "redirect:/prefeitura/painel";
        }
        return "redirect:/sabesp/chamados";
    }

    @GetMapping("/cidadao/novo")
    public String novoChamado() {
        return "redirect:/chamados/novo";
    }

    @GetMapping("/cidadao/chamados")
    public String meusChamados(Model model, ChamadoService service) {
        var chamados = service.listarTodos();
        model.addAttribute("chamados", chamados);
        model.addAttribute("total", chamados.size());
        model.addAttribute("andamento", chamados.stream()
                .filter(chamado -> !"Resolvido".equals(chamado.getStatus()))
                .count());
        model.addAttribute("resolvidos", chamados.stream()
                .filter(chamado -> "Resolvido".equals(chamado.getStatus()))
                .count());
        return "chamados/acompanhamento";
    }

    @GetMapping("/sabesp/chamados")
    public String filaSabesp() {
        return "redirect:/chamados?perfil=sabesp";
    }

    @GetMapping("/prefeitura/chamados")
    public String ocorrenciasPrefeitura() {
        return "redirect:/chamados?perfil=prefeitura";
    }

    @GetMapping("/prefeitura/painel")
    public String painel(Model model, ChamadoService service) {
        var chamados = service.listarTodos();
        model.addAttribute("total", chamados.size());
        model.addAttribute("novos", chamados.stream()
                .filter(chamado -> "Novo".equals(chamado.getStatus())
                        || "Aberto".equals(chamado.getStatus()))
                .count());
        model.addAttribute("atendimento", chamados.stream()
                .filter(chamado -> "Em atendimento".equals(chamado.getStatus()))
                .count());
        model.addAttribute("resolvidos", chamados.stream()
                .filter(chamado -> "Resolvido".equals(chamado.getStatus()))
                .count());
        model.addAttribute("chamados", chamados);
        return "chamados/painel";
    }
}
