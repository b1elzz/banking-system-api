package com.org.fundatec.sistemabancario.service;

import com.org.fundatec.sistemabancario.dto.BancoDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.model.Banco;
import com.org.fundatec.sistemabancario.repository.BancoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BancoService {

    @Autowired
    private BancoRepository bancoRepository;

    @Transactional
    public BancoDTO salvar(BancoDTO bancoDTO) {
        Banco banco = new Banco(bancoDTO.getCodigo(), bancoDTO.getNome(), bancoDTO.getCnpj());
        Banco bancoSalvo = bancoRepository.save(banco);
        return converterParaDTO(bancoSalvo);
    }

    public BancoDTO buscarPorId(Long id) {
        Banco banco = bancoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Banco não encontrado com ID: " + id));
        return converterParaDTO(banco);
    }

    public BancoDTO buscarPorCodigo(Integer codigo) {
        Banco banco = bancoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Banco não encontrado com código: " + codigo));
        return converterParaDTO(banco);
    }

    public List<BancoDTO> buscarPorNome(String nome) {
        return bancoRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<BancoDTO> listarTodos() {
        return bancoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletar(Long id) {
        if (!bancoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Banco não encontrado com ID: " + id);
        }
        bancoRepository.deleteById(id);
    }

    @Transactional
    public BancoDTO atualizar(Long id, BancoDTO bancoDTO) {
        Banco banco = bancoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Banco não encontrado com ID: " + id));

        banco.setCodigo(bancoDTO.getCodigo());
        banco.setNome(bancoDTO.getNome());
        banco.setCnpj(bancoDTO.getCnpj());

        return converterParaDTO(bancoRepository.save(banco));
    }

    private BancoDTO converterParaDTO(Banco banco) {
        return new BancoDTO(banco.getId(), banco.getCodigo(), banco.getNome(), banco.getCnpj());
    }
}