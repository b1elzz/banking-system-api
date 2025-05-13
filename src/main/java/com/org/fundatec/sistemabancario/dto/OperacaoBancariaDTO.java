package com.org.fundatec.sistemabancario.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class OperacaoBancariaDTO {
    @NotNull
    @Positive
    private BigDecimal valor;

    @NotNull
    private Long contaId;

    public OperacaoBancariaDTO() {
    }

    public OperacaoBancariaDTO(BigDecimal valor, Long contaId) {
        this.valor = valor;
        this.contaId = contaId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Long getContaId() {
        return contaId;
    }

    public void setContaId(Long contaId) {
        this.contaId = contaId;
    }
}