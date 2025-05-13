package com.org.fundatec.sistemabancario.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class OperacaoBancariaDTO {

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private BigDecimal valor;

    @NotNull(message = "Número da conta é obrigatório")
    private Integer numeroConta;


    public BigDecimal getValor() {
        return valor;
    }

    public Integer getNumeroConta() {
        return numeroConta;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setNumeroConta(Integer numeroConta) {
        this.numeroConta = numeroConta;
    }
}