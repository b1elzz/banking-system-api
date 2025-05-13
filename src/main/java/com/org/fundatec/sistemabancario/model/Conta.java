package com.org.fundatec.sistemabancario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "CONTA")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotNull(message = "Número é obrigatório")
    @Column(name = "NUMERO", unique = true)
    private Integer numero;

    @PositiveOrZero(message = "Saldo não pode ser negativo")
    @Column(name = "SALDO")
    private BigDecimal saldo = BigDecimal.ZERO;

    @NotNull(message = "Cliente é obrigatório")
    @ManyToOne
    @JoinColumn(name = "CLIENTE_ID")
    private Cliente cliente;

    @NotNull(message = "Agência é obrigatória")
    @ManyToOne
    @JoinColumn(name = "AGENCIA_ID")
    private Agencia agencia;

    public Conta() {
    }

    public Conta(Integer numero, Cliente cliente, Agencia agencia) {
        this.numero = numero;
        this.cliente = cliente;
        this.agencia = agencia;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Conta conta = (Conta) o;
        return Objects.equals(id, conta.id) &&
                Objects.equals(numero, conta.numero) &&
                Objects.equals(saldo, conta.saldo) &&
                Objects.equals(cliente, conta.cliente) &&
                Objects.equals(agencia, conta.agencia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numero, saldo, cliente, agencia);
    }
}