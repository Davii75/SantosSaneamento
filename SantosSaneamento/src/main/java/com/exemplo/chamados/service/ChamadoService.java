package com.exemplo.chamados.service;

import com.exemplo.chamados.model.Chamado;
import com.exemplo.chamados.repository.ChamadoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;

    public ChamadoService(ChamadoRepository chamadoRepository) {
        this.chamadoRepository = chamadoRepository;
    }

    public Chamado salvar(Chamado chamado) {
        return chamadoRepository.save(chamado);
    }

    public List<Chamado> listarTodos() {
        return chamadoRepository.findAllByOrderByDataHoraDesc();
    }

    public List<Chamado> listarDoSolicitante(String solicitante) {
        return chamadoRepository.findBySolicitanteOrderByDataHoraDesc(solicitante);
    }

    public Optional<Chamado> buscarPorId(Long id) {
        return chamadoRepository.findById(id);
    }

    /** Busca o chamado ou responde 404 (página "não encontrado"), em vez de dar erro 500. */
    public Chamado buscarOuFalhar(Long id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chamado não encontrado: " + id));
    }

    public void excluir(Long id) {
        chamadoRepository.deleteById(id);
    }

    /**
     * Filtros da prefeitura/Sabesp. Qualquer parâmetro vazio é ignorado.
     * A busca procura no protocolo (#CH-12), endereço, bairro, referência e tipo.
     */
    public List<Chamado> filtrar(String busca, String status, String bairro, String tipo) {
        String termo = busca == null ? "" : busca.trim().toLowerCase(Locale.ROOT);

        return listarTodos().stream()
                .filter(c -> vazio(status) || status.equals(c.getStatus()))
                .filter(c -> vazio(bairro) || bairro.equals(c.getBairro()))
                .filter(c -> vazio(tipo) || tipo.equals(c.getTipo()))
                .filter(c -> termo.isEmpty() || textoBusca(c).contains(termo))
                .toList();
    }

    public long contarPorStatus(List<Chamado> chamados, String status) {
        return chamados.stream().filter(c -> status.equals(c.getStatus())).count();
    }

    private String textoBusca(Chamado c) {
        return ("#ch-" + c.getId() + " " + nulo(c.getEndereco()) + " " + nulo(c.getBairro()) + " "
                + nulo(c.getReferencia()) + " " + nulo(c.getTipo())).toLowerCase(Locale.ROOT);
    }

    private String nulo(String texto) {
        return texto == null ? "" : texto;
    }

    private boolean vazio(String texto) {
        return texto == null || texto.isBlank();
    }
}
