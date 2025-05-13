package com.org.fundatec.sistemabancario.service;

import com.org.fundatec.sistemabancario.dto.ClienteDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.model.Cliente;
import com.org.fundatec.sistemabancario.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public ClienteDTO salvar(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente(clienteDTO.getCpf(), clienteDTO.getNome());
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return converterParaDTO(clienteSalvo);
    }

    public ClienteDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado com ID: " + id));
        return converterParaDTO(cliente);
    }

    public ClienteDTO buscarPorCpf(String cpf) {
        Cliente cliente = clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado com CPF: " + cpf));
        return converterParaDTO(cliente);
    }

    public List<ClienteDTO> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Cliente não encontrado com ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

    @Transactional
    public ClienteDTO atualizar(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado com ID: " + id));

        cliente.setCpf(clienteDTO.getCpf());
        cliente.setNome(clienteDTO.getNome());

        return converterParaDTO(clienteRepository.save(cliente));
    }

    private ClienteDTO converterParaDTO(Cliente cliente) {
        return new ClienteDTO(cliente.getId(), cliente.getCpf(), cliente.getNome());
    }
}