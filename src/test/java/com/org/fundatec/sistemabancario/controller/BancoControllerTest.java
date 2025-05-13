package com.org.fundatec.sistemabancario.controller;

import com.org.fundatec.sistemabancario.dto.BancoDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.service.BancoService;
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

class BancoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BancoService bancoService;

    @InjectMocks
    private BancoController bancoController;

    private final ObjectMapper objectMapper = new ObjectMapper();


    private static final String CNPJ_VALIDO = "11444777000161";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bancoController)
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

        BancoDTO request = new BancoDTO(null, 1, "Banco Teste", CNPJ_VALIDO);
        BancoDTO response = new BancoDTO(1L, 1, "Banco Teste", CNPJ_VALIDO);

        when(bancoService.salvar(any(BancoDTO.class))).thenReturn(response);


        mockMvc.perform(post("/bancos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void criar_ComCNPJInvalido_DeveRetornar400() throws Exception {

        BancoDTO request = new BancoDTO(null, 1, "Banco Teste", "123");


        mockMvc.perform(post("/bancos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarPorId_ComIdExistente_DeveRetornar200() throws Exception {

        BancoDTO response = new BancoDTO(1L, 1, "Banco Teste", CNPJ_VALIDO);
        when(bancoService.buscarPorId(1L)).thenReturn(response);


        mockMvc.perform(get("/bancos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.codigo").value(1));
    }

    @Test
    void buscarPorId_ComIdInexistente_DeveRetornar404() throws Exception {

        when(bancoService.buscarPorId(1L)).thenThrow(new EntidadeNaoEncontradaException("Banco não encontrado"));


        mockMvc.perform(get("/bancos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorCodigo_ComCodigoExistente_DeveRetornar200() throws Exception {

        BancoDTO response = new BancoDTO(1L, 1, "Banco Teste", CNPJ_VALIDO);
        when(bancoService.buscarPorCodigo(1)).thenReturn(response);

        mockMvc.perform(get("/bancos/por-codigo/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value(1));
    }

    @Test
    void listarTodos_ComBancosExistentes_DeveRetornar200() throws Exception {

        BancoDTO banco = new BancoDTO(1L, 1, "Banco Teste", CNPJ_VALIDO);
        when(bancoService.listarTodos()).thenReturn(List.of(banco));


        mockMvc.perform(get("/bancos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Banco Teste"));
    }

    @Test
    void buscarPorNome_ComTextoExistente_DeveRetornar200() throws Exception {

        BancoDTO banco = new BancoDTO(1L, 1, "Banco Teste", CNPJ_VALIDO);
        when(bancoService.buscarPorNome("teste")).thenReturn(List.of(banco));

        mockMvc.perform(get("/bancos/buscar-por-nome").param("nome", "teste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Banco Teste"));
    }

    @Test
    void atualizar_ComDadosValidos_DeveRetornar200() throws Exception {

        BancoDTO request = new BancoDTO(null, 1, "Banco Atualizado", CNPJ_VALIDO);
        BancoDTO response = new BancoDTO(1L, 1, "Banco Atualizado", CNPJ_VALIDO);

        when(bancoService.atualizar(eq(1L), any(BancoDTO.class))).thenReturn(response);

        mockMvc.perform(put("/bancos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Banco Atualizado"));
    }

    @Test
    void deletar_ComIdExistente_DeveRetornar204() throws Exception {

        doNothing().when(bancoService).deletar(1L);


        mockMvc.perform(delete("/bancos/1"))
                .andExpect(status().isNoContent());
    }
}