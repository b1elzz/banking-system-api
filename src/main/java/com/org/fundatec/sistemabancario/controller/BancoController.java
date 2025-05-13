package com.org.fundatec.sistemabancario.controller;

import com.org.fundatec.sistemabancario.dto.BancoDTO;
import com.org.fundatec.sistemabancario.service.BancoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/bancos")
public class BancoController {

    @Autowired
    private BancoService bancoService;

    @PostMapping
    public ResponseEntity<BancoDTO> criar(@RequestBody @Valid BancoDTO bancoDTO) {
        BancoDTO bancoSalvo = bancoService.salvar(bancoDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bancoSalvo.getId())
                .toUri();
        return ResponseEntity.created(location).body(bancoSalvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BancoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bancoService.buscarPorId(id));
    }

    @GetMapping("/por-codigo/{codigo}")
    public ResponseEntity<BancoDTO> buscarPorCodigo(@PathVariable Integer codigo) {
        return ResponseEntity.ok(bancoService.buscarPorCodigo(codigo));
    }

    @GetMapping
    public ResponseEntity<List<BancoDTO>> listarTodos() {
        return ResponseEntity.ok(bancoService.listarTodos());
    }

    @GetMapping("/buscar-por-nome")
    public ResponseEntity<List<BancoDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(bancoService.buscarPorNome(nome));
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<BancoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid BancoDTO bancoDTO) {
        return ResponseEntity.ok(bancoService.atualizar(id, bancoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        bancoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}