package com.org.fundatec.sistemabancario.controller;

import com.org.fundatec.sistemabancario.dto.ContaDTO;
import com.org.fundatec.sistemabancario.dto.OperacaoBancariaDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.service.ContaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ContaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ContaService contaService;

    @InjectMocks
    private ContaController contaController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @RestControllerAdvice
    static class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
        @ExceptionHandler(EntidadeNaoEncontradaException.class)
        public ResponseEntity<Object> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Test
    void criar_ComDadosValidos_DeveRetornar201() throws Exception {

        ContaDTO request = new ContaDTO(null, 12345, BigDecimal.ZERO, 1L, 1L);
        ContaDTO response = new ContaDTO(1L, 12345, BigDecimal.ZERO, 1L, 1L);

        when(contaService.salvar(any(ContaDTO.class))).thenReturn(response);


        mockMvc.perform(post("/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.numero").value(12345))
                .andExpect(jsonPath("$.saldo").value(0))
                .andExpect(jsonPath("$.clienteId").value(1L))
                .andExpect(jsonPath("$.agenciaId").value(1L));
    }

    @Test
    void buscarPorNumero_ComNumeroExistente_DeveRetornar200() throws Exception {

        ContaDTO response = new ContaDTO(1L, 12345, new BigDecimal("1000.00"), 1L, 1L);
        when(contaService.buscarPorNumero(12345)).thenReturn(response);

        mockMvc.perform(get("/contas/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.numero").value(12345))
                .andExpect(jsonPath("$.saldo").value(1000.00))
                .andExpect(jsonPath("$.clienteId").value(1L))
                .andExpect(jsonPath("$.agenciaId").value(1L));
    }

    @Test
    void buscarPorNumero_ComNumeroInexistente_DeveRetornar404() throws Exception {

        when(contaService.buscarPorNumero(99999)).thenThrow(new EntidadeNaoEncontradaException("Conta não encontrada"));


        mockMvc.perform(get("/contas/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void depositar_ComDadosValidos_DeveRetornar204() throws Exception {

        OperacaoBancariaDTO request = new OperacaoBancariaDTO(new BigDecimal("500.00"), 1L);
        doNothing().when(contaService).depositar(any(OperacaoBancariaDTO.class));


        mockMvc.perform(post("/contas/depositos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void sacar_ComDadosValidos_DeveRetornar204() throws Exception {

        OperacaoBancariaDTO request = new OperacaoBancariaDTO(new BigDecimal("300.00"), 1L);
        doNothing().when(contaService).sacar(any(OperacaoBancariaDTO.class));


        mockMvc.perform(post("/contas/saques")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletar_ComIdExistente_DeveRetornar204() throws Exception {

        doNothing().when(contaService).deletar(1L);


        mockMvc.perform(delete("/contas/1"))
                .andExpect(status().isNoContent());
    }
}