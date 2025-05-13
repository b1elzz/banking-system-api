package com.org.fundatec.sistemabancario.service;

import com.org.fundatec.sistemabancario.dto.ClienteDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.model.Cliente;
import com.org.fundatec.sistemabancario.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void salvar_ComDadosValidos_DeveRetornarClienteDTO() {

        Cliente clienteSalvo = new Cliente("12345678901", "Fulano da Silva");
        clienteSalvo.setId(1L);

        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);

        ClienteDTO resultado = clienteService.salvar(
                new ClienteDTO(null, "12345678901", "Fulano da Silva"));


        assertAll(
                () -> assertEquals(1L, resultado.getId()),
                () -> assertEquals("12345678901", resultado.getCpf()),
                () -> assertEquals("Fulano da Silva", resultado.getNome())
        );
    }

    @Test
    void buscarPorId_ComIdExistente_DeveRetornarClienteDTO() {

        Cliente cliente = new Cliente("12345678901", "Fulano da Silva");
        cliente.setId(1L);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));


        ClienteDTO resultado = clienteService.buscarPorId(1L);


        assertAll(
                () -> assertEquals(1L, resultado.getId()),
                () -> assertEquals("12345678901", resultado.getCpf())
        );
    }

    @Test
    void buscarPorId_ComIdInexistente_DeveLancarExcecao() {

        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(EntidadeNaoEncontradaException.class, () ->
                clienteService.buscarPorId(1L));
    }

    @Test
    void buscarPorCpf_ComCpfExistente_DeveRetornarClienteDTO() {

        Cliente cliente = new Cliente("12345678901", "Fulano da Silva");

        when(clienteRepository.findByCpf("12345678901")).thenReturn(Optional.of(cliente));


        ClienteDTO resultado = clienteService.buscarPorCpf("12345678901");


        assertEquals("Fulano da Silva", resultado.getNome());
    }

    @Test
    void buscarPorCpf_ComCpfInexistente_DeveLancarExcecao() {

        when(clienteRepository.findByCpf("12345678901")).thenReturn(Optional.empty());


        assertThrows(EntidadeNaoEncontradaException.class, () ->
                clienteService.buscarPorCpf("12345678901"));
    }

    @Test
    void buscarPorNome_ComTextoExistente_DeveRetornarLista() {

        Cliente cliente = new Cliente("12345678901", "Fulano da Silva");

        when(clienteRepository.findByNomeContainingIgnoreCase("Fulano"))
                .thenReturn(List.of(cliente));


        List<ClienteDTO> resultado = clienteService.buscarPorNome("Fulano");


        assertAll(
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("12345678901", resultado.get(0).getCpf())
        );
    }

    @Test
    void atualizar_ComDadosValidos_DeveRetornarClienteAtualizado() {

        Cliente clienteExistente = new Cliente("12345678901", "Nome Antigo");
        clienteExistente.setId(1L);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteExistente);


        ClienteDTO resultado = clienteService.atualizar(1L,
                new ClienteDTO(null, "12345678901", "Nome Novo"));


        assertEquals("Nome Novo", resultado.getNome());
    }

    @Test
    void deletar_ComIdExistente_DeveExecutarComSucesso() {

        when(clienteRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> clienteService.deletar(1L));
        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void deletar_ComIdInexistente_DeveLancarExcecao() {

        when(clienteRepository.existsById(1L)).thenReturn(false);


        assertThrows(EntidadeNaoEncontradaException.class, () ->
                clienteService.deletar(1L));
        verify(clienteRepository, never()).deleteById(any());
    }
}