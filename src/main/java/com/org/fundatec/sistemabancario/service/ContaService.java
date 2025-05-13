package com.org.fundatec.sistemabancario.service;

import com.org.fundatec.sistemabancario.dto.ContaDTO;
import com.org.fundatec.sistemabancario.dto.OperacaoBancariaDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.exception.OperacaoInvalidaException;
import com.org.fundatec.sistemabancario.model.Agencia;
import com.org.fundatec.sistemabancario.model.Cliente;
import com.org.fundatec.sistemabancario.model.Conta;
import com.org.fundatec.sistemabancario.repository.AgenciaRepository;
import com.org.fundatec.sistemabancario.repository.ClienteRepository;
import com.org.fundatec.sistemabancario.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Transactional
    public ContaDTO salvar(ContaDTO contaDTO) {
        Cliente cliente = clienteRepository.findById(contaDTO.getClienteId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado com ID: " + contaDTO.getClienteId()));

        Agencia agencia = agenciaRepository.findById(contaDTO.getAgenciaId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Agência não encontrada com ID: " + contaDTO.getAgenciaId()));

        Conta conta = new Conta(contaDTO.getNumero(), cliente, agencia);
        Conta contaSalva = contaRepository.save(conta);
        return converterParaDTO(contaSalva);
    }

    public ContaDTO buscarPorNumero(Integer numero) {
        Conta conta = contaRepository.findByNumero(numero)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta não encontrada com número: " + numero));
        return converterParaDTO(conta);
    }

    @Transactional
    public void deletar(Long id) {
        if (!contaRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Conta não encontrada com ID: " + id);
        }
        contaRepository.deleteById(id);
    }

    @Transactional
    public void depositar(OperacaoBancariaDTO operacaoDTO) {
        if (operacaoDTO.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OperacaoInvalidaException("Valor do depósito deve ser positivo");
        }

        Conta conta = contaRepository.findById(operacaoDTO.getContaId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta não encontrada com ID: " + operacaoDTO.getContaId()));

        conta.setSaldo(conta.getSaldo().add(operacaoDTO.getValor()));
        contaRepository.save(conta);
    }

    @Transactional
    public void sacar(OperacaoBancariaDTO operacaoDTO) {
        if (operacaoDTO.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OperacaoInvalidaException("Valor do saque deve ser positivo");
        }

        Conta conta = contaRepository.findById(operacaoDTO.getContaId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta não encontrada com ID: " + operacaoDTO.getContaId()));

        BigDecimal novoSaldo = conta.getSaldo().subtract(operacaoDTO.getValor());
        if (novoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new OperacaoInvalidaException("Saldo insuficiente para realizar o saque");
        }

        conta.setSaldo(novoSaldo);
        contaRepository.save(conta);
    }

    private ContaDTO converterParaDTO(Conta conta) {
        return new ContaDTO(
                conta.getId(),
                conta.getNumero(),
                conta.getSaldo(),
                conta.getCliente().getId(),
                conta.getAgencia().getId());
    }
}