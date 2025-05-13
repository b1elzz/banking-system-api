package com.org.fundatec.sistemabancario.controller;

import com.org.fundatec.sistemabancario.dto.AgenciaDTO;
import com.org.fundatec.sistemabancario.model.Agencia;
import com.org.fundatec.sistemabancario.service.AgenciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/agencias", produces = MediaType.APPLICATION_JSON_VALUE)
public class AgenciaController {

    @Autowired
    private AgenciaService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Agencia> criar(@RequestBody @Valid AgenciaDTO agenciaDTO) {
        Agencia agenciaSalva = service.salvar(agenciaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(agenciaSalva);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Agencia> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping(value = "/numero/{numero}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Agencia> buscarPorNumero(@PathVariable Integer numero) {
        return ResponseEntity.ok(service.buscarPorNumero(numero));
    }

    @GetMapping(value = "/banco/{bancoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Agencia>> buscarPorBanco(@PathVariable Long bancoId) {
        return ResponseEntity.ok(service.buscarPorBanco(bancoId));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Agencia> atualizar(@PathVariable Long id,
                                             @RequestBody @Valid AgenciaDTO agenciaDTO) {
        return ResponseEntity.ok(service.atualizar(id, agenciaDTO));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
