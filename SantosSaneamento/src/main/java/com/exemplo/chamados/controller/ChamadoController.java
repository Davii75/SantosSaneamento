package com.exemplo.chamados.controller;

import com.exemplo.chamados.model.Chamado;
import com.exemplo.chamados.service.ChamadoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService service;

    public ChamadoController(ChamadoService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(@RequestParam(defaultValue = "sabesp") String perfil, Model model) {
        var chamados = service.listarTodos();
        model.addAttribute("chamados", chamados);
        model.addAttribute("perfil", perfil);
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
        return "chamados/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("chamado", new Chamado());
        return "chamados/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Chamado chamado) {
        if (chamado.getStatus() == null || chamado.getStatus().isBlank()) {
            chamado.setStatus("Novo");
        }
        service.salvar(chamado);
        return "redirect:/cidadao/chamados";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("chamado", service.buscarPorId(id).orElseThrow());
        return "chamados/form";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhes(
            @PathVariable Long id,
            @RequestParam(defaultValue = "cidadao") String perfil,
            Model model) {
        model.addAttribute("chamado", service.buscarPorId(id).orElseThrow());
        model.addAttribute("perfil", perfil);
        return "chamados/detalhes";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute Chamado dados,
            @RequestParam String perfil) {
        Chamado chamado = service.buscarPorId(id).orElseThrow();
        chamado.setStatus(dados.getStatus());
        chamado.setComentarioSabesp(dados.getComentarioSabesp());
        service.salvar(chamado);
        return "redirect:/chamados/detalhes/" + id + "?perfil=" + perfil;
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        service.excluir(id);
        return "redirect:/chamados?perfil=prefeitura";
    }
}
