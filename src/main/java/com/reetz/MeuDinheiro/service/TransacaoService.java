package com.reetz.MeuDinheiro.service;

import com.reetz.MeuDinheiro.model.Transacao;
import com.reetz.MeuDinheiro.repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService {

        private final TransacaoRepository transacaoRepository;

        public TransacaoService(TransacaoRepository transacaoRepository) {
            this.transacaoRepository = transacaoRepository;
        }

        public Transacao salvar(Transacao transacao) {
            return transacaoRepository.save(transacao);
        }

        public List<Transacao> listarTodas() {
            return transacaoRepository.findAll();
        }

        public Optional<Transacao> buscarPorId(Long id) {
            return transacaoRepository.findById(id);
        }

        public void deletar(Long id) {
            transacaoRepository.deleteById(id);
        }
}
