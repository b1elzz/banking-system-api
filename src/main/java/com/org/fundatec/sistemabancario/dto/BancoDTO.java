package com.org.fundatec.sistemabancario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CNPJ;

public class BancoDTO {
    private Long id;

    @NotNull
    private Integer codigo;

    @NotBlank
    private String nome;

    @CNPJ
    @NotBlank
    private String cnpj;

    public BancoDTO() {
    }

    public BancoDTO(Long id, Integer codigo, String nome, String cnpj) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.cnpj = cnpj;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}