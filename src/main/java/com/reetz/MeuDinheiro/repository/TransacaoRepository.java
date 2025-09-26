package com.reetz.MeuDinheiro.repository;

import com.reetz.MeuDinheiro.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByUsuarioId(Long usuarioId);

    List<Transacao> findByCategoriaId(Long categoriaId);
}
