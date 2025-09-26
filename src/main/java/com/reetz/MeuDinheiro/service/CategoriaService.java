package com.reetz.MeuDinheiro.service;

import com.reetz.MeuDinheiro.repository.CategoriaRepository;
import com.reetz.MeuDinheiro.model.Categoria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria salvar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    public void deletar(Long id) {
        categoriaRepository.deleteById(id);
    }
}
