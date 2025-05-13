package com.org.fundatec.sistemabancario.service;

import com.org.fundatec.sistemabancario.dto.AgenciaDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.model.Agencia;
import com.org.fundatec.sistemabancario.model.Banco;
import com.org.fundatec.sistemabancario.repository.AgenciaRepository;
import com.org.fundatec.sistemabancario.repository.BancoRepository;
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
class AgenciaServiceTest {

    @Mock
    private AgenciaRepository agenciaRepository;

    @Mock
    private BancoRepository bancoRepository;

    @InjectMocks
    private AgenciaService agenciaService;

    @Test
    void salvar_ComDadosValidos_DeveRetornarAgenciaDTO() {

        Banco banco = new Banco(1, "Banco Teste", "12345678000199");
        banco.setId(1L);
        Agencia agenciaSalva = new Agencia(1234, "Agência Centro", banco);
        agenciaSalva.setId(1L);

        when(bancoRepository.findById(1L)).thenReturn(Optional.of(banco));
        when(agenciaRepository.save(any(Agencia.class))).thenReturn(agenciaSalva);


        AgenciaDTO resultado = agenciaService.salvar(
                new AgenciaDTO(null, 1234, "Agência Centro", 1L));

        assertEquals(1L, resultado.getId());
        assertEquals(1234, resultado.getNumero());
        verify(agenciaRepository).save(any(Agencia.class));
    }

    @Test
    void salvar_ComBancoInexistente_DeveLancarExcecao() {
        when(bancoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () ->
                agenciaService.salvar(new AgenciaDTO(null, 1234, "Agência Centro", 1L)));
    }

    @Test
    void buscarPorId_ComIdExistente_DeveRetornarAgenciaDTO() {

        Banco banco = new Banco(1, "Banco Teste", "12345678000199");
        banco.setId(1L);

        Agencia agencia = new Agencia(1234, "Agência Centro", banco);
        agencia.setId(1L);

        when(agenciaRepository.findById(1L)).thenReturn(Optional.of(agencia));


        AgenciaDTO resultado = agenciaService.buscarPorId(1L);


        assertEquals(1L, resultado.getId()); // Agora vai passar
        assertEquals("Agência Centro", resultado.getNome());
        assertEquals(1L, resultado.getBancoId());
    }

    @Test
    void buscarPorId_ComIdInexistente_DeveLancarExcecao() {
        when(agenciaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () ->
                agenciaService.buscarPorId(1L));
    }

    @Test
    void buscarPorNumero_ComNumeroExistente_DeveRetornarAgenciaDTO() {
        Banco banco = new Banco(1, "Banco Teste", "12345678000199");
        Agencia agencia = new Agencia(1234, "Agência Centro", banco);

        when(agenciaRepository.findByNumero(1234)).thenReturn(Optional.of(agencia));

        AgenciaDTO resultado = agenciaService.buscarPorNumero(1234);

        assertEquals(1234, resultado.getNumero());
    }

    @Test
    void buscarPorNumero_ComNumeroInexistente_DeveLancarExcecao() {
        when(agenciaRepository.findByNumero(1234)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () ->
                agenciaService.buscarPorNumero(1234));
    }

    @Test
    void buscarPorBanco_ComBancoExistente_DeveRetornarLista() {
        Banco banco = new Banco(1, "Banco Teste", "12345678000199");
        Agencia agencia = new Agencia(1234, "Agência Centro", banco);

        when(agenciaRepository.findByBancoId(1L)).thenReturn(List.of(agencia));

        List<AgenciaDTO> resultado = agenciaService.buscarPorBanco(1L);

        assertFalse(resultado.isEmpty());
        assertEquals("Agência Centro", resultado.get(0).getNome());
    }

    @Test
    void atualizar_ComDadosValidos_DeveRetornarAgenciaAtualizada() {

        Banco bancoExistente = new Banco(1, "Banco Teste", "12345678000199");
        bancoExistente.setId(1L);

        Agencia agenciaExistente = new Agencia(1234, "Agência Antiga", bancoExistente);
        agenciaExistente.setId(1L);

        when(agenciaRepository.findById(1L)).thenReturn(Optional.of(agenciaExistente));
        when(bancoRepository.findById(1L)).thenReturn(Optional.of(bancoExistente));
        when(agenciaRepository.save(any(Agencia.class))).thenReturn(agenciaExistente);


        AgenciaDTO resultado = agenciaService.atualizar(1L,
                new AgenciaDTO(null, 5678, "Agência Nova", 1L));


        assertEquals("Agência Nova", resultado.getNome());
        assertEquals(5678, resultado.getNumero());
    }

    @Test
    void atualizar_ComIdInexistente_DeveLancarExcecao() {
        when(agenciaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () ->
                agenciaService.atualizar(1L, new AgenciaDTO(null, 1234, "Agência Centro", 1L)));
    }

    @Test
    void deletar_ComIdExistente_DeveExecutarComSucesso() {
        when(agenciaRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> agenciaService.deletar(1L));
        verify(agenciaRepository).deleteById(1L);
    }

    @Test
    void deletar_ComIdInexistente_DeveLancarExcecao() {
        when(agenciaRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntidadeNaoEncontradaException.class, () ->
                agenciaService.deletar(1L));
        verify(agenciaRepository, never()).deleteById(any());
    }
}