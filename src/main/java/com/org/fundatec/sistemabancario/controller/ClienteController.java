package com.org.fundatec.sistemabancario.controller;

import com.org.fundatec.sistemabancario.dto.ClienteDTO;
import com.org.fundatec.sistemabancario.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteDTO> criar(@RequestBody @Valid ClienteDTO clienteDTO) {
        ClienteDTO clienteSalvo = clienteService.salvar(clienteDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clienteSalvo.getId())
                .toUri();
        return ResponseEntity.created(location).body(clienteSalvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @GetMapping("/por-cpf/{cpf}")
    public ResponseEntity<ClienteDTO> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(clienteService.buscarPorCpf(cpf));
    }

    @GetMapping("/buscar-por-nome")
    public ResponseEntity<List<ClienteDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(clienteService.buscarPorNome(nome));
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ClienteDTO clienteDTO) {
        return ResponseEntity.ok(clienteService.atualizar(id, clienteDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}