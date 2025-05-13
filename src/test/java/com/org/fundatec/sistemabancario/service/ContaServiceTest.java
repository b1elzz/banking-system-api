package com.org.fundatec.sistemabancario.service;

import com.org.fundatec.sistemabancario.dto.ContaDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.exception.OperacaoInvalidaException;
import com.org.fundatec.sistemabancario.model.Agencia;
import com.org.fundatec.sistemabancario.model.Cliente;
import com.org.fundatec.sistemabancario.model.Conta;
import com.org.fundatec.sistemabancario.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private AgenciaService agenciaService;

    @InjectMocks
    private ContaService contaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveSalvarContaComSucesso() {
        ContaDTO dto = new ContaDTO();
        dto.setNumero(12345);
        dto.setSaldo(BigDecimal.ZERO);
        dto.setClienteId(1L);
        dto.setAgenciaId(1L);

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        Agencia agencia = new Agencia();
        agencia.setId(1L);

        Conta contaSalva = new Conta();
        contaSalva.setNumero(12345);
        contaSalva.setSaldo(BigDecimal.ZERO);
        contaSalva.setCliente(cliente);
        contaSalva.setAgencia(agencia);
        contaSalva.setId(1L);

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(agenciaService.buscarPorId(1L)).thenReturn(agencia);
        when(contaRepository.save(any(Conta.class))).thenReturn(contaSalva);

        Conta resultado = contaService.salvar(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(12345, resultado.getNumero());
        assertEquals(cliente, resultado.getCliente());
        assertEquals(agencia, resultado.getAgencia());
    }

    @Test
    void deveBuscarContaPorNumero() {
        Conta conta = new Conta();
        conta.setNumero(12345);
        conta.setSaldo(BigDecimal.valueOf(1000));
        conta.setId(1L);

        when(contaRepository.findByNumero(12345)).thenReturn(Optional.of(conta));

        Conta resultado = contaService.buscarPorNumero(12345);

        assertNotNull(resultado);
        assertEquals(12345, resultado.getNumero());
        assertEquals(BigDecimal.valueOf(1000), resultado.getSaldo());
    }

    @Test
    void deveLancarExcecaoQuandoContaNaoEncontradaPorNumero() {
        when(contaRepository.findByNumero(12345)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            contaService.buscarPorNumero(12345);
        });
    }

    @Test
    void deveDepositarComSucesso() {
        Conta conta = new Conta();
        conta.setNumero(12345);
        conta.setSaldo(BigDecimal.valueOf(500));

        when(contaRepository.findByNumero(12345)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        assertDoesNotThrow(() -> contaService.depositar(12345, BigDecimal.valueOf(300)));
        assertEquals(BigDecimal.valueOf(800), conta.getSaldo());
    }

    @Test
    void deveLancarExcecaoAoDepositarEmContaInexistente() {
        when(contaRepository.findByNumero(12345)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            contaService.depositar(12345, BigDecimal.valueOf(100));
        });
    }

    @Test
    void deveSacarComSucesso() {
        Conta conta = new Conta();
        conta.setNumero(12345);
        conta.setSaldo(BigDecimal.valueOf(1000));

        when(contaRepository.findByNumero(12345)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        assertDoesNotThrow(() -> contaService.sacar(12345, BigDecimal.valueOf(300)));
        assertEquals(BigDecimal.valueOf(700), conta.getSaldo());
    }

    @Test
    void deveLancarExcecaoAoSacarDeContaInexistente() {
        when(contaRepository.findByNumero(12345)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            contaService.sacar(12345, BigDecimal.valueOf(100));
        });
    }

    @Test
    void deveLancarExcecaoAoSacarComSaldoInsuficiente() {
        Conta conta = new Conta();
        conta.setNumero(12345);
        conta.setSaldo(BigDecimal.valueOf(100));

        when(contaRepository.findByNumero(12345)).thenReturn(Optional.of(conta));

        assertThrows(OperacaoInvalidaException.class, () -> {
            contaService.sacar(12345, BigDecimal.valueOf(200));
        });
    }

    @Test
    void deveDeletarContaComSucesso() {
        when(contaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(contaRepository).deleteById(1L);

        assertDoesNotThrow(() -> contaService.deletar(1L));
        verify(contaRepository).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoDeletarContaInexistente() {
        when(contaRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntidadeNaoEncontradaException.class, () -> contaService.deletar(1L));
    }
}