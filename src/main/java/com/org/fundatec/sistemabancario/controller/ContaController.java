package com.org.fundatec.sistemabancario.controller;

import com.org.fundatec.sistemabancario.dto.ContaDTO;
import com.org.fundatec.sistemabancario.dto.OperacaoBancariaDTO;
import com.org.fundatec.sistemabancario.model.Conta;
import com.org.fundatec.sistemabancario.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/contas", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContaController {

    @Autowired
    private ContaService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Conta> criar(@RequestBody @Valid ContaDTO contaDTO) {
        Conta contaSalva = service.salvar(contaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(contaSalva);
    }

    @GetMapping(value = "/{numero}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Conta> buscarPorNumero(@PathVariable Integer numero) {
        return ResponseEntity.ok(service.buscarPorNumero(numero));
    }

    @PostMapping(value = "/depositar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> depositar(@RequestBody @Valid OperacaoBancariaDTO dto) {
        service.depositar(dto.getNumeroConta(), dto.getValor());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/sacar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sacar(@RequestBody @Valid OperacaoBancariaDTO dto) {
        service.sacar(dto.getNumeroConta(), dto.getValor());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
