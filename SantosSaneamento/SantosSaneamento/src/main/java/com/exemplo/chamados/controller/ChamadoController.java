package com.exemplo.chamados.controller;

import com.exemplo.chamados.model.Chamado;
import com.exemplo.chamados.security.AutenticacaoInterceptor;
import com.exemplo.chamados.security.Usuario;
import com.exemplo.chamados.service.ChamadoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/chamados")
public class ChamadoController {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final ChamadoService service;

    @Value("${app.upload-dir:uploads}")
    private String pastaUploads;

    public ChamadoController(ChamadoService service) {
        this.service = service;
    }

    // ---------- listas de opções disponíveis em todas as telas deste controller ----------

    /** Bairros agrupados por região (usados nos selects do formulário e do filtro). */
    @ModelAttribute("bairrosPorRegiao")
    public Map<String, List<String>> bairrosPorRegiao() {
        return Chamado.BAIRROS_POR_REGIAO;
    }

    @ModelAttribute("tipos")
    public List<String> tipos() {
        return Chamado.TIPOS;
    }

    @ModelAttribute("statusLista")
    public List<String> statusLista() {
        return Chamado.STATUS;
    }

    /**
     * O formulário só pode preencher estes campos. Status, comentário, solicitante e foto
     * nunca vêm do navegador (assim ninguém consegue forjar um POST e mudar o status).
     */
    @InitBinder("chamado")
    public void camposPermitidos(WebDataBinder binder) {
        binder.setAllowedFields("id", "endereco", "bairro", "referencia", "tipo", "descricao");
    }

    // ---------- Sabesp e prefeitura: lista com filtros ----------

    @GetMapping
    public String listar(@RequestParam(required = false) String busca,
                         @RequestParam(required = false) String status,
                         @RequestParam(required = false) String bairro,
                         @RequestParam(required = false) String tipo,
                         Model model) {
        var todos = service.listarTodos();
        var filtrados = service.filtrar(busca, status, bairro, tipo);

        model.addAttribute("chamados", filtrados);
        model.addAttribute("busca", busca);
        model.addAttribute("statusSel", status);
        model.addAttribute("bairroSel", bairro);
        model.addAttribute("tipoSel", tipo);

        model.addAttribute("total", todos.size());
        model.addAttribute("novos", service.contarPorStatus(todos, "Novo"));
        model.addAttribute("atendimento", service.contarPorStatus(todos, "Em atendimento"));
        model.addAttribute("resolvidos", service.contarPorStatus(todos, "Resolvido"));
        return "chamados/lista";
    }

    // ---------- Cidadão: registrar e editar ----------

    @GetMapping("/novo")
    public String novo(Model model) {
        Chamado chamado = new Chamado();
        model.addAttribute("chamado", chamado);
        dadosDoFormulario(model, chamado, null, null);
        return "chamados/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, HttpSession sessao, Model model) {
        Chamado chamado = service.buscarOuFalhar(id);
        if (!podeEditar(usuarioLogado(sessao), chamado)) {
            return "redirect:/cidadao/chamados";
        }
        model.addAttribute("chamado", chamado);
        dadosDoFormulario(model, chamado, null, null);
        return "chamados/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("chamado") Chamado chamado,
                         BindingResult result,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
                         @RequestParam(required = false) MultipartFile foto,
                         HttpSession sessao,
                         Model model,
                         RedirectAttributes redirect) throws IOException {

        Usuario usuario = usuarioLogado(sessao);

        // Edição: confere se o chamado existe, é da própria pessoa e ainda está "Novo"
        Chamado existente = null;
        if (chamado.getId() != null) {
            existente = service.buscarOuFalhar(chamado.getId());
            if (!podeEditar(usuario, existente)) {
                return "redirect:/cidadao/chamados";
            }
        }

        // Validações que as annotations não cobrem
        if (!result.hasFieldErrors("bairro") && !Chamado.BAIRROS.contains(chamado.getBairro())) {
            result.rejectValue("bairro", "bairro.invalido", "Selecione um bairro válido.");
        }
        if (!result.hasFieldErrors("tipo") && !Chamado.TIPOS.contains(chamado.getTipo())) {
            result.rejectValue("tipo", "tipo.invalido", "Selecione um tipo de problema válido.");
        }

        LocalDateTime dataHora = null;
        if (data == null || hora == null) {
            result.rejectValue("dataHora", "dataHora.obrigatoria", "Informe a data e a hora.");
        } else {
            dataHora = LocalDateTime.of(data, hora);
            if (dataHora.isAfter(LocalDateTime.now().plusMinutes(1))) {
                result.rejectValue("dataHora", "dataHora.futura", "A data e a hora não podem estar no futuro.");
            }
        }

        boolean fotoEnviada = foto != null && !foto.isEmpty();
        String erroFoto = null;
        if (fotoEnviada && !fotoValida(foto)) {
            erroFoto = "A foto precisa ser JPG ou PNG.";
        }

        if (result.hasErrors() || erroFoto != null) {
            dadosDoFormulario(model, chamado, data, hora);
            model.addAttribute("erroFoto", erroFoto);
            return "chamados/form";
        }

        Chamado destino;
        if (existente == null) {
            destino = chamado;
            destino.setSolicitante(usuario.getNome());
            destino.setStatus("Novo");
        } else {
            destino = existente;
            destino.setEndereco(chamado.getEndereco());
            destino.setBairro(chamado.getBairro());
            destino.setReferencia(chamado.getReferencia());
            destino.setTipo(chamado.getTipo());
            destino.setDescricao(chamado.getDescricao());
        }
        destino.setDataHora(dataHora);
        if (fotoEnviada) {
            destino.setImagem(guardarFoto(foto));
        }
        service.salvar(destino);

        redirect.addFlashAttribute("sucesso",
                existente == null ? "Chamado registrado com sucesso!" : "Chamado atualizado com sucesso!");
        return "redirect:/cidadao/chamados";
    }

    // ---------- Todos os perfis: detalhes ----------

    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id, HttpSession sessao, Model model) {
        Chamado chamado = service.buscarOuFalhar(id);
        Usuario usuario = usuarioLogado(sessao);

        // o cidadão só enxerga os próprios chamados
        if ("cidadao".equals(usuario.getPerfil()) && !ehDono(usuario, chamado)) {
            return "redirect:/cidadao/chamados";
        }
        model.addAttribute("chamado", chamado);
        model.addAttribute("podeEditar", podeEditar(usuario, chamado));
        return "chamados/detalhes";
    }

    // ---------- Sabesp: status e comentário ----------

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id,
                            @RequestParam String status,
                            @RequestParam(required = false) String comentarioSabesp,
                            RedirectAttributes redirect) {
        Chamado chamado = service.buscarOuFalhar(id);

        if (!Chamado.STATUS.contains(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido.");
        }
        String comentario = comentarioSabesp == null ? "" : comentarioSabesp.trim();
        if (comentario.length() > 1500) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comentário muito longo.");
        }

        chamado.setStatus(status);
        chamado.setComentarioSabesp(comentario.isEmpty() ? null : comentario);
        service.salvar(chamado);

        redirect.addFlashAttribute("sucesso", "Atendimento atualizado com sucesso!");
        return "redirect:/chamados/detalhes/" + id;
    }

    // ---------- Prefeitura: excluir (POST, nunca por link) ----------

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        service.buscarOuFalhar(id);
        service.excluir(id);
        redirect.addFlashAttribute("sucesso", "Chamado excluído.");
        return "redirect:/chamados";
    }

    // ---------- métodos auxiliares ----------

    private Usuario usuarioLogado(HttpSession sessao) {
        return (Usuario) sessao.getAttribute(AutenticacaoInterceptor.SESSAO_USUARIO);
    }

    private boolean ehDono(Usuario usuario, Chamado chamado) {
        return usuario.getNome().equals(chamado.getSolicitante());
    }

    /** Só o dono do chamado edita, e só enquanto o status ainda é "Novo". */
    private boolean podeEditar(Usuario usuario, Chamado chamado) {
        return "cidadao".equals(usuario.getPerfil()) && ehDono(usuario, chamado) && chamado.podeSerEditado();
    }

    /** Título e valores de data/hora do formulário (mantém o que a pessoa digitou se houve erro). */
    private void dadosDoFormulario(Model model, Chamado chamado, LocalDate data, LocalTime hora) {
        model.addAttribute("titulo", chamado.getId() == null ? "Registrar novo chamado" : "Editar chamado");
        LocalDateTime base = chamado.getDataHora() != null ? chamado.getDataHora() : LocalDateTime.now();
        model.addAttribute("dataValor", (data != null ? data : base.toLocalDate()).toString());
        model.addAttribute("horaValor", (hora != null ? hora : base.toLocalTime()).format(HORA));
    }

    private boolean fotoValida(MultipartFile foto) {
        String tipo = foto.getContentType();
        return "image/jpeg".equals(tipo) || "image/png".equals(tipo);
    }

    /** Salva a foto na pasta de uploads e devolve só o nome do arquivo. */
    private String guardarFoto(MultipartFile foto) throws IOException {
        String extensao = "image/png".equals(foto.getContentType()) ? ".png" : ".jpg";
        String nome = UUID.randomUUID() + extensao;
        Path pasta = Paths.get(pastaUploads).toAbsolutePath();
        Files.createDirectories(pasta);
        foto.transferTo(pasta.resolve(nome));
        return nome;
    }
}
