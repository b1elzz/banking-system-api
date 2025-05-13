package com.org.fundatec.sistemabancario.controller;

import com.org.fundatec.sistemabancario.dto.ClienteDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.model.Cliente;
import com.org.fundatec.sistemabancario.service.ClienteService;
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

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveCadastrarClienteComSucesso() throws Exception {
        ClienteDTO dto = new ClienteDTO();
        dto.setCpf("123.456.789-09");
        dto.setNome("Fulano de Tal");

        Cliente clienteSalvo = new Cliente("123.456.789-09", "Fulano de Tal");
        clienteSalvo.setId(1L);

        Mockito.when(clienteService.salvar(any(ClienteDTO.class))).thenReturn(clienteSalvo);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cpf").value("123.456.789-09"))
                .andExpect(jsonPath("$.nome").value("Fulano de Tal"));
    }

    @Test
    void deveRetornarErroQuandoNomeNulo() throws Exception {
        ClienteDTO dto = new ClienteDTO();
        dto.setCpf("123.456.789-09");
        dto.setNome(null);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveBuscarClientePorId() throws Exception {
        Cliente cliente = new Cliente("123.456.789-09", "Fulano de Tal");
        cliente.setId(1L);

        Mockito.when(clienteService.buscarPorId(1L)).thenReturn(cliente);

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cpf").value("123.456.789-09"));
    }

    @Test
    void deveRetornarNotFoundParaIdInexistente() throws Exception {
        Mockito.when(clienteService.buscarPorId(1L))
                .thenThrow(new EntidadeNaoEncontradaException("Cliente não encontrado"));

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveBuscarClientePorCpf() throws Exception {
        Cliente cliente = new Cliente("123.456.789-09", "Fulano de Tal");
        Mockito.when(clienteService.buscarPorCpf("123.456.789-09")).thenReturn(cliente);

        mockMvc.perform(get("/clientes/cpf/123.456.789-09"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Fulano de Tal"));
    }

    @Test
    void deveBuscarClientesPorNome() throws Exception {
        Cliente cliente = new Cliente("123.456.789-09", "Fulano de Tal");
        Mockito.when(clienteService.buscarPorNome("Fulano")).thenReturn(List.of(cliente));

        mockMvc.perform(get("/clientes/buscar?nome=Fulano"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cpf").value("123.456.789-09"));
    }

    @Test
    void deveAtualizarCliente() throws Exception {
        ClienteDTO dto = new ClienteDTO();
        dto.setCpf("987.654.321-00");
        dto.setNome("Nome Atualizado");

        Cliente clienteAtualizado = new Cliente("987.654.321-00", "Nome Atualizado");
        clienteAtualizado.setId(1L);

        Mockito.when(clienteService.atualizar(Mockito.eq(1L), any(ClienteDTO.class))).thenReturn(clienteAtualizado);

        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"));
    }

    @Test
    void deveDeletarClienteComSucesso() throws Exception {
        Mockito.doNothing().when(clienteService).deletar(1L);

        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoDeletarClienteInexistente() throws Exception {
        Mockito.doThrow(new EntidadeNaoEncontradaException("Cliente não encontrado"))
                .when(clienteService).deletar(1L);

        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isNotFound());
    }
}