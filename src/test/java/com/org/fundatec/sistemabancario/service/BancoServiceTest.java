package com.org.fundatec.sistemabancario.service;

import com.org.fundatec.sistemabancario.dto.BancoDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.model.Banco;
import com.org.fundatec.sistemabancario.repository.BancoRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
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
class BancoServiceTest {

    @Mock
    private BancoRepository bancoRepository;

    @InjectMocks
    private BancoService bancoService;

    @Test
    void salvar_ComDadosValidos_DeveRetornarBancoDTO() {
        Banco bancoSalvo = new Banco(1, "Banco Teste", "12345678000199");
        bancoSalvo.setId(1L);
        when(bancoRepository.save(any(Banco.class))).thenReturn(bancoSalvo);

        BancoDTO resultado = bancoService.salvar(new BancoDTO(null, 1, "Banco Teste", "12345678000199"));

        assertEquals(1L, resultado.getId());
        assertEquals("Banco Teste", resultado.getNome());
        verify(bancoRepository).save(any(Banco.class));
    }

    @Test
    void buscarPorId_ComIdExistente_DeveRetornarBancoDTO() {
        Banco banco = new Banco(1, "Banco Teste", "12345678000199");
        banco.setId(1L);
        when(bancoRepository.findById(1L)).thenReturn(Optional.of(banco));

        BancoDTO resultado = bancoService.buscarPorId(1L);

        assertEquals(1, resultado.getCodigo());
        assertEquals("12345678000199", resultado.getCnpj());
    }

    @Test
    void buscarPorId_ComIdInexistente_DeveLancarExcecao() {
        when(bancoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> bancoService.buscarPorId(1L));
    }

    @Test
    void buscarPorCodigo_ComCodigoExistente_DeveRetornarBancoDTO() {
        Banco banco = new Banco(1, "Banco Teste", "12345678000199");
        when(bancoRepository.findByCodigo(1)).thenReturn(Optional.of(banco));

        BancoDTO resultado = bancoService.buscarPorCodigo(1);

        assertEquals("Banco Teste", resultado.getNome());
    }

    @Test
    void buscarPorCodigo_ComCodigoInexistente_DeveLancarExcecao() {
        when(bancoRepository.findByCodigo(1)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> bancoService.buscarPorCodigo(1));
    }

    @Test
    void buscarPorNome_ComTextoExistente_DeveRetornarLista() {
        Banco banco = new Banco(1, "Banco Teste", "12345678000199");
        when(bancoRepository.findByNomeContainingIgnoreCase("teste")).thenReturn(List.of(banco));

        List<BancoDTO> resultado = bancoService.buscarPorNome("teste");

        assertEquals(1, resultado.size());
        assertEquals("Banco Teste", resultado.get(0).getNome());
    }

    @Test
    void listarTodos_ComBancosExistentes_DeveRetornarLista() {
        Banco banco = new Banco(1, "Banco Teste", "12345678000199");
        when(bancoRepository.findAll()).thenReturn(List.of(banco));

        List<BancoDTO> resultado = bancoService.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.get(0).getCodigo());
    }

    @Test
    void atualizar_ComDadosValidos_DeveRetornarBancoAtualizado() {
        Banco bancoExistente = new Banco(1, "Banco Antigo", "12345678000199");
        bancoExistente.setId(1L);

        when(bancoRepository.findById(1L)).thenReturn(Optional.of(bancoExistente));
        when(bancoRepository.save(any(Banco.class))).thenReturn(bancoExistente);

        BancoDTO resultado = bancoService.atualizar(1L,
                new BancoDTO(null, 1, "Banco Novo", "12345678000199"));

        assertEquals("Banco Novo", resultado.getNome());
    }

    @Test
    void atualizar_ComIdInexistente_DeveLancarExcecao() {
        when(bancoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () ->
                bancoService.atualizar(1L, new BancoDTO(null, 1, "Banco Novo", "12345678000199")));
    }

    @Test
    void deletar_ComIdExistente_DeveExecutarComSucesso() {
        when(bancoRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> bancoService.deletar(1L));
        verify(bancoRepository).deleteById(1L);
    }

    @Test
    void deletar_ComIdInexistente_DeveLancarExcecao() {
        when(bancoRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntidadeNaoEncontradaException.class, () -> bancoService.deletar(1L));
        verify(bancoRepository, never()).deleteById(any());
    }


}