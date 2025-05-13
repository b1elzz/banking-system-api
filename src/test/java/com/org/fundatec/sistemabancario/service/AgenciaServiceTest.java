package com.org.fundatec.sistemabancario.service;

import com.org.fundatec.sistemabancario.dto.AgenciaDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.model.Agencia;
import com.org.fundatec.sistemabancario.model.Banco;
import com.org.fundatec.sistemabancario.repository.AgenciaRepository;
import com.org.fundatec.sistemabancario.repository.BancoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AgenciaServiceTest {

    @Mock
    private AgenciaRepository agenciaRepository;

    @Mock
    private BancoRepository bancoRepository;

    @InjectMocks
    private AgenciaService agenciaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveSalvarAgenciaComSucesso() {

        AgenciaDTO dto = new AgenciaDTO();
        dto.setNumero(1234);
        dto.setNome("Centro");
        dto.setBancoId(1L);

        Banco banco = new Banco(1, "Banco Teste", "00.000.000/0001-00");
        Agencia agenciaSalva = new Agencia(1234, "Centro", banco);
        agenciaSalva.setId(1L);

        when(bancoRepository.findById(1L)).thenReturn(Optional.of(banco));
        when(agenciaRepository.save(any(Agencia.class))).thenReturn(agenciaSalva);


        Agencia resultado = agenciaService.salvar(dto);


        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1234, resultado.getNumero());
        assertEquals("Centro", resultado.getNome());
        assertEquals(banco, resultado.getBanco());
    }

    @Test
    void deveLancarExcecaoAoSalvarAgenciaComBancoInexistente() {
        AgenciaDTO dto = new AgenciaDTO();
        dto.setNumero(1234);
        dto.setNome("Centro");
        dto.setBancoId(1L);

        when(bancoRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            agenciaService.salvar(dto);
        });
    }

    @Test
    void deveBuscarAgenciaPorId() {

        Agencia agencia = new Agencia(1234, "Centro", null);
        agencia.setId(1L);

        when(agenciaRepository.findById(1L)).thenReturn(Optional.of(agencia));


        Agencia resultado = agenciaService.buscarPorId(1L);


        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1234, resultado.getNumero());
        assertEquals("Centro", resultado.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoAgenciaNaoEncontradaPorId() {

        when(agenciaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            agenciaService.buscarPorId(1L);
        });
    }

    @Test
    void deveBuscarAgenciaPorNumero() {

        Agencia agencia = new Agencia(1234, "Centro", null);
        when(agenciaRepository.findByNumero(1234)).thenReturn(Optional.of(agencia));


        Agencia resultado = agenciaService.buscarPorNumero(1234);


        assertNotNull(resultado);
        assertEquals(1234, resultado.getNumero());
        assertEquals("Centro", resultado.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoAgenciaNaoEncontradaPorNumero() {

        when(agenciaRepository.findByNumero(1234)).thenReturn(Optional.empty());


        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            agenciaService.buscarPorNumero(1234);
        });
    }

    @Test
    void deveListarAgenciasPorBanco() {

        Agencia agencia = new Agencia(1234, "Centro", null);
        when(bancoRepository.existsById(1L)).thenReturn(true);
        when(agenciaRepository.findByBancoId(1L)).thenReturn(List.of(agencia));


        List<Agencia> resultado = agenciaService.buscarPorBanco(1L);


        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(1234, resultado.get(0).getNumero());
    }

    @Test
    void deveLancarExcecaoAoListarAgenciasDeBancoInexistente() {

        when(bancoRepository.existsById(1L)).thenReturn(false);


        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            agenciaService.buscarPorBanco(1L);
        });
    }

    @Test
    void deveAtualizarAgenciaComSucesso() {

        Agencia agenciaExistente = new Agencia(1234, "Antiga", null);
        agenciaExistente.setId(1L);

        AgenciaDTO dto = new AgenciaDTO();
        dto.setNumero(9999);
        dto.setNome("Atualizada");
        dto.setBancoId(1L);

        Banco banco = new Banco(1, "Banco Teste", "00.000.000/0001-00");

        when(agenciaRepository.findById(1L)).thenReturn(Optional.of(agenciaExistente));
        when(bancoRepository.findById(1L)).thenReturn(Optional.of(banco));
        when(agenciaRepository.save(any(Agencia.class))).thenReturn(agenciaExistente);


        Agencia resultado = agenciaService.atualizar(1L, dto);


        assertEquals("Atualizada", resultado.getNome());
        assertEquals(9999, resultado.getNumero());
        assertEquals(banco, resultado.getBanco());
    }

    @Test
    void deveLancarExcecaoAoAtualizarAgenciaInexistente() {

        AgenciaDTO dto = new AgenciaDTO();
        dto.setNumero(9999);
        dto.setNome("Atualizada");
        dto.setBancoId(1L);

        when(agenciaRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            agenciaService.atualizar(1L, dto);
        });
    }

    @Test
    void deveLancarExcecaoAoAtualizarComBancoInexistente() {

        Agencia agenciaExistente = new Agencia(1234, "Antiga", null);
        agenciaExistente.setId(1L);

        AgenciaDTO dto = new AgenciaDTO();
        dto.setNumero(9999);
        dto.setNome("Atualizada");
        dto.setBancoId(1L);

        when(agenciaRepository.findById(1L)).thenReturn(Optional.of(agenciaExistente));
        when(bancoRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            agenciaService.atualizar(1L, dto);
        });
    }

    @Test
    void deveDeletarAgenciaComSucesso() {

        when(agenciaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(agenciaRepository).deleteById(1L);


        assertDoesNotThrow(() -> agenciaService.deletar(1L));
        verify(agenciaRepository).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoDeletarAgenciaInexistente() {

        when(agenciaRepository.existsById(1L)).thenReturn(false);
        assertThrows(EntidadeNaoEncontradaException.class, () -> agenciaService.deletar(1L));
    }

    @Test
    void deveRetornarListaVaziaSeBancoNaoTemAgencias() {
        when(bancoRepository.existsById(1L)).thenReturn(true);
        when(agenciaRepository.findByBancoId(1L)).thenReturn(Collections.emptyList());

        List<Agencia> resultado = agenciaService.buscarPorBanco(1L);

        assertTrue(resultado.isEmpty());
    }

}