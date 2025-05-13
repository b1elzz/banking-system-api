package com.org.fundatec.sistemabancario.controller;

import com.org.fundatec.sistemabancario.dto.ContaDTO;
import com.org.fundatec.sistemabancario.dto.OperacaoBancariaDTO;
import com.org.fundatec.sistemabancario.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping
    public ResponseEntity<ContaDTO> criar(@RequestBody @Valid ContaDTO contaDTO) {
        ContaDTO contaSalva = contaService.salvar(contaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(contaSalva.getId())
                .toUri();
        return ResponseEntity.created(location).body(contaSalva);
    }

    @GetMapping("/{numero}")
    public ResponseEntity<ContaDTO> buscarPorNumero(@PathVariable Integer numero) {
        return ResponseEntity.ok(contaService.buscarPorNumero(numero));
    }

    @Transactional
    @PostMapping("/depositos")
    public ResponseEntity<Void> depositar(@RequestBody @Valid OperacaoBancariaDTO operacaoDTO) {
        contaService.depositar(operacaoDTO);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PostMapping("/saques")
    public ResponseEntity<Void> sacar(@RequestBody @Valid OperacaoBancariaDTO operacaoDTO) {
        contaService.sacar(operacaoDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        contaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}