package com.org.fundatec.sistemabancario.controller;

import com.org.fundatec.sistemabancario.dto.AgenciaDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.model.Agencia;
import com.org.fundatec.sistemabancario.service.AgenciaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgenciaController.class)
public class AgenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgenciaService agenciaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveCadastrarAgenciaComSucesso() throws Exception {
        AgenciaDTO dto = new AgenciaDTO();
        dto.setNumero(1234);
        dto.setNome("Centro");
        dto.setBancoId(1L);

        Agencia agenciaSalva = new Agencia(1234, "Centro", null);
        agenciaSalva.setId(1L);

        Mockito.when(agenciaService.salvar(any(AgenciaDTO.class))).thenReturn(agenciaSalva);

        mockMvc.perform(post("/agencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deveRetornarErroQuandoNumeroNulo() throws Exception {
        AgenciaDTO dto = new AgenciaDTO();
        dto.setNumero(null);
        dto.setNome("Centro");
        dto.setBancoId(1L);

        mockMvc.perform(post("/agencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value(400))
                .andExpect(jsonPath("$.mensagem").value("numero - Número é obrigatório;"));

    }

    @Test
    void deveBuscarAgenciaPorId() throws Exception {
        Agencia agencia = new Agencia(1234, "Centro", null);
        agencia.setId(1L);

        Mockito.when(agenciaService.buscarPorId(1L)).thenReturn(agencia);

        mockMvc.perform(get("/agencias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value(1234));
    }

    @Test
    void deveRetornarNotFoundParaIdInexistente() throws Exception {
        Mockito.when(agenciaService.buscarPorId(1L))
                .thenThrow(new EntidadeNaoEncontradaException("Agência não encontrada"));

        mockMvc.perform(get("/agencias/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value(404))
                .andExpect(jsonPath("$.mensagem").value("Agência não encontrada"));
    }

    @Test
    void deveBuscarAgenciaPorNumero() throws Exception {
        Agencia agencia = new Agencia(1234, "Centro", null);
        Mockito.when(agenciaService.buscarPorNumero(1234)).thenReturn(agencia);

        mockMvc.perform(get("/agencias/numero/1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Centro"));
    }

    @Test
    void deveListarAgenciasPorBanco() throws Exception {
        Agencia agencia = new Agencia(1234, "Centro", null);
        Mockito.when(agenciaService.buscarPorBanco(1L)).thenReturn(List.of(agencia));

        mockMvc.perform(get("/agencias/banco/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deveAtualizarAgencia() throws Exception {
        AgenciaDTO dto = new AgenciaDTO();
        dto.setNumero(9999);
        dto.setNome("Atualizada");
        dto.setBancoId(1L);

        Agencia agenciaAtualizada = new Agencia(9999, "Atualizada", null);
        agenciaAtualizada.setId(1L);

        Mockito.when(agenciaService.atualizar(Mockito.eq(1L), any(AgenciaDTO.class))).thenReturn(agenciaAtualizada);

        mockMvc.perform(put("/agencias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Atualizada"));
    }

    @Test
    void deveDeletarAgenciaComSucesso() throws Exception {
        Mockito.doNothing().when(agenciaService).deletar(1L);

        mockMvc.perform(delete("/agencias/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoDeletarAgenciaInexistente() throws Exception {
        Mockito.doThrow(new EntidadeNaoEncontradaException("Agência não encontrada"))
                .when(agenciaService).deletar(1L);

        mockMvc.perform(delete("/agencias/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value(404))
                .andExpect(jsonPath("$.mensagem").value("Agência não encontrada"));
    }
}
