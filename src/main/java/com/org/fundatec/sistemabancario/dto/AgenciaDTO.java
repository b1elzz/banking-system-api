package com.org.fundatec.sistemabancario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AgenciaDTO {
    private Long id;

    @NotNull
    private Integer numero;

    @NotBlank
    private String nome;

    @NotNull
    private Long bancoId;

    public AgenciaDTO() {
    }

    public AgenciaDTO(Long id, Integer numero, String nome, Long bancoId) {
        this.id = id;
        this.numero = numero;
        this.nome = nome;
        this.bancoId = bancoId;
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

    public Long getBancoId() {
        return bancoId;
    }

    public void setBancoId(Long bancoId) {
        this.bancoId = bancoId;
    }
}