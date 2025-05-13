package com.org.fundatec.sistemabancario.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public class ClienteDTO {
    private Long id;

    @CPF
    @NotBlank
    private String cpf;

    @NotBlank
    private String nome;

    public ClienteDTO() {
    }

    public ClienteDTO(Long id, String cpf, String nome) {
        this.id = id;
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
}