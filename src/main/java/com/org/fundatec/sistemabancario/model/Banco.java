package com.org.fundatec.sistemabancario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CNPJ;
import java.util.Objects;

@Entity
@Table(name = "BANCO")
public class Banco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotNull(message = "Código é obrigatório")
    @Column(name = "CODIGO", unique = true)
    private Integer codigo;

    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "NOME")
    private String nome;

    @CNPJ(message = "CNPJ inválido")
    @NotBlank(message = "CNPJ é obrigatório")
    @Column(name = "CNPJ")
    private String cnpj;

    public Banco() {
    }

    public Banco(Integer codigo, String nome, String cnpj) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Banco banco = (Banco) o;
        return Objects.equals(id, banco.id) &&
                Objects.equals(codigo, banco.codigo) &&
                Objects.equals(nome, banco.nome) &&
                Objects.equals(cnpj, banco.cnpj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo, nome, cnpj);
    }
}