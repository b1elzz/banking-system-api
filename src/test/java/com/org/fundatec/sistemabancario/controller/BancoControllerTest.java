package com.org.fundatec.sistemabancario.controller;

import com.org.fundatec.sistemabancario.dto.BancoDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.model.Banco;
import com.org.fundatec.sistemabancario.service.BancoService;
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

@WebMvcTest(BancoController.class)
public class BancoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BancoService bancoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveCadastrarBancoComSucesso() throws Exception {
        BancoDTO dto = new BancoDTO();
        dto.setCodigo(341);
        dto.setNome("Itaú");
        dto.setCnpj("60.872.504/0001-23");

        Banco bancoSalvo = new Banco(341, "Itaú", "60.872.504/0001-23");
        bancoSalvo.setId(1L);

        Mockito.when(bancoService.salvar(any(BancoDTO.class))).thenReturn(bancoSalvo);

        mockMvc.perform(post("/bancos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deveRetornarErroQuandoNomeNulo() throws Exception {
        BancoDTO dto = new BancoDTO();
        dto.setCodigo(341);
        dto.setNome(null);
        dto.setCnpj("60.872.504/0001-23");

        mockMvc.perform(post("/bancos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value(400))
                .andExpect(jsonPath("$.mensagem").value("nome - Nome é obrigatório;"));
    }

    @Test
    void deveBuscarBancoPorId() throws Exception {
        Banco banco = new Banco(341, "Itaú", "60.872.504/0001-23");
        banco.setId(1L);

        Mockito.when(bancoService.buscarPorId(1L)).thenReturn(banco);

        mockMvc.perform(get("/bancos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value(341))
                .andExpect(jsonPath("$.nome").value("Itaú"));
    }

    @Test
    void deveRetornarNotFoundParaIdInexistente() throws Exception {
        Mockito.when(bancoService.buscarPorId(1L))
                .thenThrow(new EntidadeNaoEncontradaException("Banco não encontrado"));

        mockMvc.perform(get("/bancos/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value(404))
                .andExpect(jsonPath("$.mensagem").value("Banco não encontrado"));
    }

    @Test
    void deveBuscarBancoPorCodigo() throws Exception {
        Banco banco = new Banco(341, "Itaú", "60.872.504/0001-23");
        Mockito.when(bancoService.buscarPorCodigo(341)).thenReturn(banco);

        mockMvc.perform(get("/bancos/codigo/341"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Itaú"));
    }

    @Test
    void deveListarTodosOsBancos() throws Exception {
        Banco banco = new Banco(341, "Itaú", "60.872.504/0001-23");
        Mockito.when(bancoService.listarTodos()).thenReturn(List.of(banco));

        mockMvc.perform(get("/bancos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deveBuscarBancoPorNome() throws Exception {
        Banco banco = new Banco(341, "Itaú", "60.872.504/0001-23");
        Mockito.when(bancoService.buscarPorNome("ita")).thenReturn(List.of(banco));

        mockMvc.perform(get("/bancos/buscar?nome=ita"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Itaú"));
    }

    @Test
    void deveAtualizarBanco() throws Exception {
        BancoDTO dto = new BancoDTO();
        dto.setCodigo(999);
        dto.setNome("Atualizado");
        dto.setCnpj("12.345.678/0001-95");

        Banco bancoAtualizado = new Banco(999, "Atualizado", "12.345.678/0001-95");
        bancoAtualizado.setId(1L);

        Mockito.when(bancoService.atualizar(Mockito.eq(1L), any(BancoDTO.class))).thenReturn(bancoAtualizado);

        mockMvc.perform(put("/bancos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Atualizado"));
    }

    @Test
    void deveDeletarBancoComSucesso() throws Exception {
        Mockito.doNothing().when(bancoService).deletar(1L);

        mockMvc.perform(delete("/bancos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoDeletarBancoInexistente() throws Exception {
        Mockito.doThrow(new EntidadeNaoEncontradaException("Banco não encontrado"))
                .when(bancoService).deletar(1L);

        mockMvc.perform(delete("/bancos/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value(404))
                .andExpect(jsonPath("$.mensagem").value("Banco não encontrado"));
    }
}
