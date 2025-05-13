package com.org.fundatec.sistemabancario.controller;

import com.org.fundatec.sistemabancario.dto.ClienteDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.service.ClienteService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ClienteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private final ObjectMapper objectMapper = new ObjectMapper();


    private static final String CPF_VALIDO = "52998224725";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
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
        ClienteDTO request = new ClienteDTO(null, CPF_VALIDO, "Fulano de Tal");
        ClienteDTO response = new ClienteDTO(1L, CPF_VALIDO, "Fulano de Tal");

        when(clienteService.salvar(any(ClienteDTO.class))).thenReturn(response);


        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cpf").value(CPF_VALIDO))
                .andExpect(jsonPath("$.nome").value("Fulano de Tal"));
    }

    @Test
    void criar_ComCPFInvalido_DeveRetornar400() throws Exception {

        ClienteDTO request = new ClienteDTO(null, "123", "Fulano de Tal");

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarPorId_ComIdExistente_DeveRetornar200() throws Exception {

        ClienteDTO response = new ClienteDTO(1L, CPF_VALIDO, "Fulano de Tal");
        when(clienteService.buscarPorId(1L)).thenReturn(response);


        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cpf").value(CPF_VALIDO))
                .andExpect(jsonPath("$.nome").value("Fulano de Tal"));
    }

    @Test
    void buscarPorId_ComIdInexistente_DeveRetornar404() throws Exception {

        when(clienteService.buscarPorId(1L)).thenThrow(new EntidadeNaoEncontradaException("Cliente não encontrado"));


        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorCpf_ComCpfExistente_DeveRetornar200() throws Exception {
        ClienteDTO response = new ClienteDTO(1L, CPF_VALIDO, "Fulano de Tal");
        when(clienteService.buscarPorCpf(CPF_VALIDO)).thenReturn(response);


        mockMvc.perform(get("/clientes/por-cpf/" + CPF_VALIDO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cpf").value(CPF_VALIDO))
                .andExpect(jsonPath("$.nome").value("Fulano de Tal"));
    }

    @Test
    void buscarPorNome_ComTextoExistente_DeveRetornar200() throws Exception {

        ClienteDTO cliente = new ClienteDTO(1L, CPF_VALIDO, "Fulano de Tal");
        when(clienteService.buscarPorNome("Fulano")).thenReturn(List.of(cliente));


        mockMvc.perform(get("/clientes/buscar-por-nome").param("nome", "Fulano"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].cpf").value(CPF_VALIDO))
                .andExpect(jsonPath("$[0].nome").value("Fulano de Tal"));
    }

    @Test
    void atualizar_ComDadosValidos_DeveRetornar200() throws Exception {

        ClienteDTO request = new ClienteDTO(null, CPF_VALIDO, "Fulano Atualizado");
        ClienteDTO response = new ClienteDTO(1L, CPF_VALIDO, "Fulano Atualizado");

        when(clienteService.atualizar(eq(1L), any(ClienteDTO.class))).thenReturn(response);


        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cpf").value(CPF_VALIDO))
                .andExpect(jsonPath("$.nome").value("Fulano Atualizado"));
    }

    @Test
    void deletar_ComIdExistente_DeveRetornar204() throws Exception {

        doNothing().when(clienteService).deletar(1L);


        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isNoContent());
    }
}