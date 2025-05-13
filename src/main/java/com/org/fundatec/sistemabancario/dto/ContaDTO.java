package com.org.fundatec.sistemabancario.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class ContaDTO {
    private Long id;

    @NotNull
    private Integer numero;

    @PositiveOrZero
    private BigDecimal saldo;

    @NotNull
    private Long clienteId;

    @NotNull
    private Long agenciaId;

    public ContaDTO() {
    }

    public ContaDTO(Long id, Integer numero, BigDecimal saldo, Long clienteId, Long agenciaId) {
        this.id = id;
        this.numero = numero;
        this.saldo = saldo;
        this.clienteId = clienteId;
        this.agenciaId = agenciaId;
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

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getAgenciaId() {
        return agenciaId;
    }

    public void setAgenciaId(Long agenciaId) {
        this.agenciaId = agenciaId;
    }
}