package com.org.fundatec.sistemabancario.service;

import com.org.fundatec.sistemabancario.dto.ContaDTO;
import com.org.fundatec.sistemabancario.dto.OperacaoBancariaDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.exception.OperacaoInvalidaException;
import com.org.fundatec.sistemabancario.model.Agencia;
import com.org.fundatec.sistemabancario.model.Banco;
import com.org.fundatec.sistemabancario.model.Cliente;
import com.org.fundatec.sistemabancario.model.Conta;
import com.org.fundatec.sistemabancario.repository.AgenciaRepository;
import com.org.fundatec.sistemabancario.repository.ClienteRepository;
import com.org.fundatec.sistemabancario.repository.ContaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private AgenciaRepository agenciaRepository;

    @InjectMocks
    private ContaService contaService;

    @Test
    void salvar_ComDadosValidos_DeveRetornarContaDTO() {

        Cliente cliente = new Cliente("12345678901", "Cliente Teste");
        cliente.setId(1L);
        Agencia agencia = new Agencia(1234, "Agência Centro", new Banco());
        agencia.setId(1L);
        Conta contaSalva = new Conta(12345, cliente, agencia);
        contaSalva.setId(1L);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(agenciaRepository.findById(1L)).thenReturn(Optional.of(agencia));
        when(contaRepository.save(any(Conta.class))).thenReturn(contaSalva);


        ContaDTO resultado = contaService.salvar(
                new ContaDTO(null, 12345, BigDecimal.ZERO, 1L, 1L));


        assertAll(
                () -> assertEquals(1L, resultado.getId()),
                () -> assertEquals(12345, resultado.getNumero()),
                () -> assertEquals(1L, resultado.getClienteId())
        );
    }

    @Test
    void salvar_ComClienteInexistente_DeveLancarExcecao() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () ->
                contaService.salvar(new ContaDTO(null, 12345, BigDecimal.ZERO, 1L, 1L)));
    }

    @Test
    void buscarPorNumero_ComNumeroExistente_DeveRetornarContaDTO() {

        Cliente cliente = new Cliente("12345678901", "Cliente Teste");
        Agencia agencia = new Agencia(1234, "Agência Centro", new Banco());
        Conta conta = new Conta(12345, cliente, agencia);
        conta.setSaldo(new BigDecimal("1000.00"));

        when(contaRepository.findByNumero(12345)).thenReturn(Optional.of(conta));


        ContaDTO resultado = contaService.buscarPorNumero(12345);


        assertAll(
                () -> assertEquals(12345, resultado.getNumero()),
                () -> assertEquals(new BigDecimal("1000.00"), resultado.getSaldo())
        );
    }

    @Test
    void depositar_ComValorPositivo_DeveAtualizarSaldo() {

        Conta conta = new Conta(12345, new Cliente(), new Agencia());
        conta.setSaldo(new BigDecimal("500.00"));

        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));


        contaService.depositar(new OperacaoBancariaDTO(new BigDecimal("200.00"), 1L));


        assertEquals(new BigDecimal("700.00"), conta.getSaldo());
        verify(contaRepository).save(conta);
    }

    @Test
    void depositar_ComValorNegativo_DeveLancarExcecao() {
        assertThrows(OperacaoInvalidaException.class, () ->
                contaService.depositar(new OperacaoBancariaDTO(new BigDecimal("-100.00"), 1L)));
    }

    @Test
    void depositar_ComValorZero_DeveLancarExcecao() {
        assertThrows(OperacaoInvalidaException.class, () ->
                contaService.depositar(new OperacaoBancariaDTO(BigDecimal.ZERO, 1L)));
    }

    @Test
    void sacar_ComSaldoSuficiente_DeveAtualizarSaldo() {

        Conta conta = new Conta(12345, new Cliente(), new Agencia());
        conta.setSaldo(new BigDecimal("500.00"));

        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));


        contaService.sacar(new OperacaoBancariaDTO(new BigDecimal("200.00"), 1L));

        assertEquals(new BigDecimal("300.00"), conta.getSaldo());
    }

    @Test
    void sacar_ComSaldoInsuficiente_DeveLancarExcecao() {

        Conta conta = new Conta(12345, new Cliente(), new Agencia());
        conta.setSaldo(new BigDecimal("100.00"));

        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));


        assertThrows(OperacaoInvalidaException.class, () ->
                contaService.sacar(new OperacaoBancariaDTO(new BigDecimal("200.00"), 1L)));
    }

    @Test
    void deletar_ComIdExistente_DeveExecutarComSucesso() {

        when(contaRepository.existsById(1L)).thenReturn(true);


        assertDoesNotThrow(() -> contaService.deletar(1L));
        verify(contaRepository).deleteById(1L);
    }

    @Test
    void deletar_ComIdInexistente_DeveLancarExcecao() {

        when(contaRepository.existsById(1L)).thenReturn(false);


        assertThrows(EntidadeNaoEncontradaException.class, () ->
                contaService.deletar(1L));
        verify(contaRepository, never()).deleteById(any());
    }
}