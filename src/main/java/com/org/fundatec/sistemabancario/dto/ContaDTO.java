package com.org.fundatec.sistemabancario.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class ContaDTO {

    @NotNull(message = "Número é obrigatório")
    private Integer numero;

    @PositiveOrZero(message = "Saldo não pode ser negativo")
    private BigDecimal saldo = BigDecimal.ZERO;

    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "ID da agência é obrigatório")
    private Long agenciaId;


    public Integer getNumero() {
        return numero;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public Long getAgenciaId() {
        return agenciaId;
    }
    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    public void setAgenciaId(Long agenciaId) {
        this.agenciaId = agenciaId;
    }
}