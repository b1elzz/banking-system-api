package com.org.fundatec.sistemabancario.service;

import com.org.fundatec.sistemabancario.dto.ContaDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.exception.OperacaoInvalidaException;
import com.org.fundatec.sistemabancario.model.Conta;
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
    private ClienteService clienteService;

    @Autowired
    private AgenciaService agenciaService;

    @Transactional
    public Conta salvar(ContaDTO contaDTO) {
        Conta conta = new Conta();
        conta.setNumero(contaDTO.getNumero());
        conta.setSaldo(contaDTO.getSaldo());
        conta.setCliente(clienteService.buscarPorId(contaDTO.getClienteId()));
        conta.setAgencia(agenciaService.buscarPorId(contaDTO.getAgenciaId()));
        return contaRepository.save(conta);
    }

    public Conta buscarPorNumero(Integer numero) {
        return contaRepository.findByNumero(numero)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta não encontrada com número: " + numero));
    }

    @Transactional
    public void depositar(Integer numero, BigDecimal valor) {
        Conta conta = buscarPorNumero(numero);
        conta.setSaldo(conta.getSaldo().add(valor));
        contaRepository.save(conta);
    }

    @Transactional
    public void sacar(Integer numero, BigDecimal valor) {
        Conta conta = buscarPorNumero(numero);

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new OperacaoInvalidaException("Saldo insuficiente para saque");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));
        contaRepository.save(conta);
    }

    @Transactional
    public void deletar(Long id) {
        if (!contaRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Conta não encontrada com ID: " + id);
        }
        contaRepository.deleteById(id);
    }
}