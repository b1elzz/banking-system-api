package com.org.fundatec.sistemabancario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "AGENCIA")
public class Agencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotNull(message = "Número é obrigatório")
    @Column(name = "NUMERO", unique = true)
    private Integer numero;

    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "NOME")
    private String nome;

    @NotNull(message = "Banco é obrigatório")
    @ManyToOne
    @JoinColumn(name = "BANCO_ID")
    private Banco banco;

    public Agencia() {
    }

    public Agencia(Integer numero, String nome, Banco banco) {
        this.numero = numero;
        this.nome = nome;
        this.banco = banco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Agencia agencia = (Agencia) o;
        return Objects.equals(id, agencia.id) &&
                Objects.equals(numero, agencia.numero) &&
                Objects.equals(nome, agencia.nome) &&
                Objects.equals(banco, agencia.banco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numero, nome, banco);
    }
}