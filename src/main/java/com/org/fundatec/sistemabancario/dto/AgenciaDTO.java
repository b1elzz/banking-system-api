package com.org.fundatec.sistemabancario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AgenciaDTO {

    @NotNull(message = "Número é obrigatório")
    private Integer numero;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "ID do banco é obrigatório")
    private Long bancoId;


    public Integer getNumero() {
        return numero;
    }

    public String getNome() {
        return nome;
    }

    public Long getBancoId() {
        return bancoId;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setBancoId(Long bancoId) {
        this.bancoId = bancoId;
    }
}