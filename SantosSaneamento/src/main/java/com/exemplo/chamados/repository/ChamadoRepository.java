package com.exemplo.chamados.repository;

import com.exemplo.chamados.model.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    /** Todos os chamados, do mais novo para o mais antigo. */
    List<Chamado> findAllByOrderByDataHoraDesc();

    /** Chamados de uma pessoa (tela "Meus chamados"). */
    List<Chamado> findBySolicitanteOrderByDataHoraDesc(String solicitante);
}
