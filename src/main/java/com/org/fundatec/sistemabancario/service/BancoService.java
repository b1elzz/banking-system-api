package com.org.fundatec.sistemabancario.service;

import com.org.fundatec.sistemabancario.dto.BancoDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.model.Banco;
import com.org.fundatec.sistemabancario.repository.BancoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BancoService {

    @Autowired
    private BancoRepository bancoRepository;

    @Transactional
    public Banco salvar(BancoDTO bancoDTO) {
        Banco banco = new Banco();
        banco.setCodigo(bancoDTO.getCodigo());
        banco.setNome(bancoDTO.getNome());
        banco.setCnpj(bancoDTO.getCnpj());
        return bancoRepository.save(banco);
    }

    public Banco buscarPorId(Long id) {
        return bancoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Banco não encontrado com ID: " + id));
    }

    public Banco buscarPorCodigo(Integer codigo) {
        return bancoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Banco não encontrado com código: " + codigo));
    }

    public List<Banco> buscarPorNome(String nome) {
        return bancoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Banco> listarTodos() {
        return bancoRepository.findAll();
    }

    @Transactional
    public Banco atualizar(Long id, BancoDTO bancoDTO) {
        Banco bancoExistente = bancoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Banco não encontrado com ID: " + id));

        bancoExistente.setCodigo(bancoDTO.getCodigo());
        bancoExistente.setNome(bancoDTO.getNome());
        bancoExistente.setCnpj(bancoDTO.getCnpj());

        return bancoRepository.save(bancoExistente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!bancoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Banco não encontrado com ID: " + id);
        }
        bancoRepository.deleteById(id);
    }
}