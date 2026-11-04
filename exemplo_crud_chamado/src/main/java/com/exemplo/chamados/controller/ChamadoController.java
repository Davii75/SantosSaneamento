package com.exemplo.chamados.controller;

import com.exemplo.chamados.model.Chamado;
import com.exemplo.chamados.service.ChamadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chamados")
public class ChamadoController {

    @Autowired
    private ChamadoService chamadoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("chamados", chamadoService.listarTodos());
        return "chamados/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("chamado", new Chamado());
        return "chamados/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Chamado chamado) {
        chamadoService.salvar(chamado);
        return "redirect:/chamados";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Chamado chamado = chamadoService.buscarPorId(id).orElse(null);
        model.addAttribute("chamado", chamado);
        return "chamados/form";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        chamadoService.excluir(id);
        return "redirect:/chamados";
    }
}
