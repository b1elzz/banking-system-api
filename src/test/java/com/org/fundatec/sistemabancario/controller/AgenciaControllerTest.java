package com.org.fundatec.sistemabancario.controller;

import com.org.fundatec.sistemabancario.dto.AgenciaDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.service.AgenciaService;
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

class AgenciaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AgenciaService agenciaService;

    @InjectMocks
    private AgenciaController agenciaController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(agenciaController)
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

        AgenciaDTO request = new AgenciaDTO(null, 1234, "Agência Central", 1L);
        AgenciaDTO response = new AgenciaDTO(1L, 1234, "Agência Central", 1L);

        when(agenciaService.salvar(any(AgenciaDTO.class))).thenReturn(response);

        mockMvc.perform(post("/agencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.numero").value(1234));
    }

    @Test
    void buscarPorId_ComIdExistente_DeveRetornar200() throws Exception {

        AgenciaDTO response = new AgenciaDTO(1L, 1234, "Agência Central", 1L);
        when(agenciaService.buscarPorId(1L)).thenReturn(response);

        mockMvc.perform(get("/agencias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.numero").value(1234));
    }

    @Test
    void buscarPorId_ComIdInexistente_DeveRetornar404() throws Exception {

        when(agenciaService.buscarPorId(1L)).thenThrow(new EntidadeNaoEncontradaException("Agência não encontrada"));


        mockMvc.perform(get("/agencias/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorNumero_ComNumeroExistente_DeveRetornar200() throws Exception {

        AgenciaDTO response = new AgenciaDTO(1L, 1234, "Agência Central", 1L);
        when(agenciaService.buscarPorNumero(1234)).thenReturn(response);


        mockMvc.perform(get("/agencias/por-numero/1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value(1234));
    }

    @Test
    void buscarPorBanco_ComBancoExistente_DeveRetornar200() throws Exception {

        AgenciaDTO agencia = new AgenciaDTO(1L, 1234, "Agência Central", 1L);
        when(agenciaService.buscarPorBanco(1L)).thenReturn(List.of(agencia));

        mockMvc.perform(get("/agencias/por-banco").param("bancoId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Agência Central"));
    }

    @Test
    void atualizar_ComDadosValidos_DeveRetornar200() throws Exception {

        AgenciaDTO request = new AgenciaDTO(null, 1234, "Agência Atualizada", 1L);
        AgenciaDTO response = new AgenciaDTO(1L, 1234, "Agência Atualizada", 1L);

        when(agenciaService.atualizar(eq(1L), any(AgenciaDTO.class))).thenReturn(response);


        mockMvc.perform(put("/agencias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Agência Atualizada"));
    }

    @Test
    void deletar_ComIdExistente_DeveRetornar204() throws Exception {

        doNothing().when(agenciaService).deletar(1L);

        mockMvc.perform(delete("/agencias/1"))
                .andExpect(status().isNoContent());
    }
}