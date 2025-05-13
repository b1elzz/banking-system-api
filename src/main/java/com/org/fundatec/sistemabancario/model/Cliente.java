package com.org.fundatec.sistemabancario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import java.util.Objects;

@Entity
@Table(name = "CLIENTE")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @CPF(message = "CPF inválido")
    @NotBlank(message = "CPF é obrigatório")
    @Column(name = "CPF", unique = true)
    private String cpf;

    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "NOME")
    private String nome;

    public Cliente() {
    }

    public Cliente(String cpf, String nome) {
        this.cpf = cpf;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id) &&
                Objects.equals(cpf, cliente.cpf) &&
                Objects.equals(nome, cliente.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cpf, nome);
    }
}