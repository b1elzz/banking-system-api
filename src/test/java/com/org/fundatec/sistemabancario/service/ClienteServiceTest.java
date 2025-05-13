package com.org.fundatec.sistemabancario.service;

import com.org.fundatec.sistemabancario.dto.ClienteDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.model.Cliente;
import com.org.fundatec.sistemabancario.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveSalvarClienteComSucesso() {
        ClienteDTO dto = new ClienteDTO();
        dto.setCpf("123.456.789-09");
        dto.setNome("Fulano de Tal");

        Cliente clienteSalvo = new Cliente("123.456.789-09", "Fulano de Tal");
        clienteSalvo.setId(1L);

        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);

        Cliente resultado = clienteService.salvar(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("123.456.789-09", resultado.getCpf());
        assertEquals("Fulano de Tal", resultado.getNome());
    }

    @Test
    void deveBuscarClientePorId() {
        Cliente cliente = new Cliente("123.456.789-09", "Fulano de Tal");
        cliente.setId(1L);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Fulano de Tal", resultado.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontradoPorId() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            clienteService.buscarPorId(1L);
        });
    }

    @Test
    void deveBuscarClientePorCpf() {
        Cliente cliente = new Cliente("123.456.789-09", "Fulano de Tal");
        when(clienteRepository.findByCpf("123.456.789-09")).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.buscarPorCpf("123.456.789-09");

        assertNotNull(resultado);
        assertEquals("Fulano de Tal", resultado.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontradoPorCpf() {
        when(clienteRepository.findByCpf("123.456.789-09")).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            clienteService.buscarPorCpf("123.456.789-09");
        });
    }

    @Test
    void deveBuscarClientesPorNome() {
        Cliente cliente = new Cliente("123.456.789-09", "Fulano de Tal");
        when(clienteRepository.findByNomeContainingIgnoreCase("Fulano")).thenReturn(List.of(cliente));

        List<Cliente> resultado = clienteService.buscarPorNome("Fulano");

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Fulano de Tal", resultado.get(0).getNome());
    }

    @Test
    void deveAtualizarClienteComSucesso() {
        Cliente clienteExistente = new Cliente("123.456.789-09", "Nome Antigo");
        clienteExistente.setId(1L);

        ClienteDTO dto = new ClienteDTO();
        dto.setCpf("987.654.321-00");
        dto.setNome("Nome Atualizado");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteExistente);

        Cliente resultado = clienteService.atualizar(1L, dto);

        assertEquals("Nome Atualizado", resultado.getNome());
        assertEquals("987.654.321-00", resultado.getCpf());
    }

    @Test
    void deveLancarExcecaoAoAtualizarClienteInexistente() {
        ClienteDTO dto = new ClienteDTO();
        dto.setCpf("987.654.321-00");
        dto.setNome("Nome Atualizado");

        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            clienteService.atualizar(1L, dto);
        });
    }

    @Test
    void deveDeletarClienteComSucesso() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(1L);

        assertDoesNotThrow(() -> clienteService.deletar(1L));
        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoDeletarClienteInexistente() {
        when(clienteRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntidadeNaoEncontradaException.class, () -> clienteService.deletar(1L));
    }
}