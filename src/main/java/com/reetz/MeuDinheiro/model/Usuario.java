package com.reetz.MeuDinheiro.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String password;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Categoria> categorias;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Transacao> transacoes;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public void setTransacoes(List<Transacao> transacoes) {
        this.transacoes = transacoes;
    }
}
