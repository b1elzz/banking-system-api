package com.org.fundatec.sistemabancario.controller;

import com.org.fundatec.sistemabancario.dto.BancoDTO;
import com.org.fundatec.sistemabancario.model.Banco;
import com.org.fundatec.sistemabancario.service.BancoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/bancos", produces = MediaType.APPLICATION_JSON_VALUE)
public class BancoController {

    @Autowired
    private BancoService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Banco> criar(@RequestBody @Valid BancoDTO bancoDTO) {
        Banco bancoSalvo = service.salvar(bancoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(bancoSalvo);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Banco> buscarPorId(@PathVariable Long id) {
        Banco banco = service.buscarPorId(id);
        return ResponseEntity.ok(banco);
    }

    @GetMapping(value = "/codigo/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Banco> buscarPorCodigo(@PathVariable Integer codigo) {
        Banco banco = service.buscarPorCodigo(codigo);
        return ResponseEntity.ok(banco);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Banco>> listarTodos() {
        List<Banco> bancos = service.listarTodos();
        return ResponseEntity.ok(bancos);
    }

    @GetMapping(value = "/buscar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Banco>> buscarPorNome(@RequestParam String nome) {
        List<Banco> bancos = service.buscarPorNome(nome);
        return ResponseEntity.ok(bancos);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Banco> atualizar(@PathVariable Long id,
                                           @RequestBody @Valid BancoDTO bancoDTO) {
        Banco bancoAtualizado = service.atualizar(id, bancoDTO);
        return ResponseEntity.ok(bancoAtualizado);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
