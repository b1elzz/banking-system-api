package com.org.fundatec.sistemabancario.service;

import com.org.fundatec.sistemabancario.dto.BancoDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.model.Banco;
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

class BancoServiceTest {

    @Mock
    private BancoRepository bancoRepository;

    @InjectMocks
    private BancoService bancoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveSalvarBancoComSucesso() {
        BancoDTO dto = new BancoDTO();
        dto.setCodigo(341);
        dto.setNome("Itaú");
        dto.setCnpj("60.872.504/0001-23");

        Banco bancoSalvo = new Banco(341, "Itaú", "60.872.504/0001-23");
        bancoSalvo.setId(1L);

        when(bancoRepository.save(any(Banco.class))).thenReturn(bancoSalvo);

        Banco resultado = bancoService.salvar(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void deveBuscarBancoPorId() {
        Banco banco = new Banco(341, "Itaú", "60.872.504/0001-23");
        banco.setId(1L);

        when(bancoRepository.findById(1L)).thenReturn(Optional.of(banco));

        Banco resultado = bancoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(341, resultado.getCodigo());
    }

    @Test
    void deveLancarExcecaoQuandoBancoNaoEncontrado() {
        when(bancoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            bancoService.buscarPorId(1L);
        });
    }

    @Test
    void deveListarTodosBancos() {
        Banco banco = new Banco(341, "Itaú", "60.872.504/0001-23");
        when(bancoRepository.findAll()).thenReturn(Collections.singletonList(banco));

        List<Banco> resultado = bancoService.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void deveBuscarBancoPorCodigo() {
        Banco banco = new Banco(341, "Itaú", "60.872.504/0001-23");
        when(bancoRepository.findByCodigo(341)).thenReturn(Optional.of(banco));

        Banco resultado = bancoService.buscarPorCodigo(341);

        assertNotNull(resultado);
        assertEquals("Itaú", resultado.getNome());
    }


    @Test
    void deveBuscarBancoPorNome() {
        Banco banco = new Banco(341, "Itaú", "60.872.504/0001-23");
        when(bancoRepository.findByNomeContainingIgnoreCase("ita")).thenReturn(List.of(banco));

        List<Banco> resultado = bancoService.buscarPorNome("ita");

        assertEquals(1, resultado.size());
        assertEquals("Itaú", resultado.get(0).getNome());
    }


    @Test
    void deveAtualizarBancoComSucesso() {
        Banco bancoExistente = new Banco(341, "Antigo", "11.111.111/0001-11");
        bancoExistente.setId(1L);

        BancoDTO dto = new BancoDTO();
        dto.setCodigo(999);
        dto.setNome("Atualizado");
        dto.setCnpj("99.999.999/0001-99");

        when(bancoRepository.findById(1L)).thenReturn(Optional.of(bancoExistente));
        when(bancoRepository.save(any(Banco.class))).thenReturn(bancoExistente);

        Banco resultado = bancoService.atualizar(1L, dto);

        assertEquals("Atualizado", resultado.getNome());
        assertEquals(999, resultado.getCodigo());
    }


    @Test
    void deveDeletarBancoComSucesso() {
        when(bancoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bancoRepository).deleteById(1L);

        assertDoesNotThrow(() -> bancoService.deletar(1L));
        verify(bancoRepository).deleteById(1L);
    }


    @Test
    void deveLancarExcecaoAoDeletarBancoInexistente() {
        when(bancoRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntidadeNaoEncontradaException.class, () -> bancoService.deletar(1L));
    }

}