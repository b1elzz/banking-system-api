package com.org.fundatec.sistemabancario.controller;

import com.org.fundatec.sistemabancario.dto.AgenciaDTO;
import com.org.fundatec.sistemabancario.service.AgenciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/agencias")
public class AgenciaController {

    @Autowired
    private AgenciaService agenciaService;

    @PostMapping
    public ResponseEntity<AgenciaDTO> criar(@RequestBody @Valid AgenciaDTO agenciaDTO) {
        AgenciaDTO agenciaSalva = agenciaService.salvar(agenciaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(agenciaSalva.getId())
                .toUri();
        return ResponseEntity.created(location).body(agenciaSalva);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgenciaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agenciaService.buscarPorId(id));
    }

    @GetMapping("/por-numero/{numero}")
    public ResponseEntity<AgenciaDTO> buscarPorNumero(@PathVariable Integer numero) {
        return ResponseEntity.ok(agenciaService.buscarPorNumero(numero));
    }

    @GetMapping("/por-banco")
    public ResponseEntity<List<AgenciaDTO>> buscarPorBanco(@RequestParam Long bancoId) {
        return ResponseEntity.ok(agenciaService.buscarPorBanco(bancoId));
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<AgenciaDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AgenciaDTO agenciaDTO) {
        return ResponseEntity.ok(agenciaService.atualizar(id, agenciaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        agenciaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}