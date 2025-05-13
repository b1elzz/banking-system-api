package com.org.fundatec.sistemabancario.controller;

import com.org.fundatec.sistemabancario.dto.ContaDTO;
import com.org.fundatec.sistemabancario.dto.OperacaoBancariaDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.exception.OperacaoInvalidaException;
import com.org.fundatec.sistemabancario.model.Conta;
import com.org.fundatec.sistemabancario.service.ContaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContaController.class)
public class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContaService contaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveCriarContaComSucesso() throws Exception {
        ContaDTO dto = new ContaDTO();
        dto.setNumero(12345);
        dto.setSaldo(BigDecimal.ZERO);
        dto.setClienteId(1L);
        dto.setAgenciaId(1L);

        Conta contaSalva = new Conta();
        contaSalva.setNumero(12345);
        contaSalva.setSaldo(BigDecimal.ZERO);
        contaSalva.setId(1L);

        Mockito.when(contaService.salvar(any(ContaDTO.class))).thenReturn(contaSalva);

        mockMvc.perform(post("/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.numero").value(12345));
    }

    @Test
    void deveBuscarContaPorNumero() throws Exception {
        Conta conta = new Conta();
        conta.setNumero(12345);
        conta.setSaldo(BigDecimal.valueOf(1000));
        conta.setId(1L);

        Mockito.when(contaService.buscarPorNumero(12345)).thenReturn(conta);

        mockMvc.perform(get("/contas/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value(12345))
                .andExpect(jsonPath("$.saldo").value(1000));
    }

    @Test
    void deveRetornarNotFoundParaContaInexistente() throws Exception {
        Mockito.when(contaService.buscarPorNumero(12345))
                .thenThrow(new EntidadeNaoEncontradaException("Conta não encontrada"));

        mockMvc.perform(get("/contas/12345"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDepositarComSucesso() throws Exception {
        OperacaoBancariaDTO dto = new OperacaoBancariaDTO();
        dto.setNumeroConta(12345);
        dto.setValor(BigDecimal.valueOf(500));

        Mockito.doNothing().when(contaService).depositar(12345, BigDecimal.valueOf(500));

        mockMvc.perform(post("/contas/depositar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornarNotFoundAoDepositarEmContaInexistente() throws Exception {
        OperacaoBancariaDTO dto = new OperacaoBancariaDTO();
        dto.setNumeroConta(12345);
        dto.setValor(BigDecimal.valueOf(500));

        Mockito.doThrow(new EntidadeNaoEncontradaException("Conta não encontrada"))
                .when(contaService).depositar(12345, BigDecimal.valueOf(500));

        mockMvc.perform(post("/contas/depositar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveSacarComSucesso() throws Exception {
        OperacaoBancariaDTO dto = new OperacaoBancariaDTO();
        dto.setNumeroConta(12345);
        dto.setValor(BigDecimal.valueOf(300));

        Mockito.doNothing().when(contaService).sacar(12345, BigDecimal.valueOf(300));

        mockMvc.perform(post("/contas/sacar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornarBadRequestAoSacarComSaldoInsuficiente() throws Exception {
        OperacaoBancariaDTO dto = new OperacaoBancariaDTO();
        dto.setNumeroConta(12345);
        dto.setValor(BigDecimal.valueOf(1000));

        Mockito.doThrow(new OperacaoInvalidaException("Saldo insuficiente"))
                .when(contaService).sacar(12345, BigDecimal.valueOf(1000));

        mockMvc.perform(post("/contas/sacar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveDeletarContaComSucesso() throws Exception {
        Mockito.doNothing().when(contaService).deletar(1L);

        mockMvc.perform(delete("/contas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoDeletarContaInexistente() throws Exception {
        Mockito.doThrow(new EntidadeNaoEncontradaException("Conta não encontrada"))
                .when(contaService).deletar(1L);

        mockMvc.perform(delete("/contas/1"))
                .andExpect(status().isNotFound());
    }
}