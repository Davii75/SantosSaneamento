package com.exemplo.chamados.service;

import com.exemplo.chamados.model.Chamado;
import com.exemplo.chamados.repository.ChamadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository chamadoRepository;

    public List<Chamado> listarTodos() {
        return chamadoRepository.findAll();
    }

    public Chamado salvar(Chamado chamado) {
        return chamadoRepository.save(chamado);
    }

    public Optional<Chamado> buscarPorId(Long id) {
        return chamadoRepository.findById(id);
    }

    public void excluir(Long id) {
        chamadoRepository.deleteById(id);
    }
}
