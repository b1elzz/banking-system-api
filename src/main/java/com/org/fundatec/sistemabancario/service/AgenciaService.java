package com.org.fundatec.sistemabancario.service;

import com.org.fundatec.sistemabancario.dto.AgenciaDTO;
import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.model.Agencia;
import com.org.fundatec.sistemabancario.model.Banco;
import com.org.fundatec.sistemabancario.repository.AgenciaRepository;
import com.org.fundatec.sistemabancario.repository.BancoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AgenciaService {

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Autowired
    private BancoRepository bancoRepository;

    @Transactional
    public Agencia salvar(AgenciaDTO agenciaDTO) {
        Banco banco = bancoRepository.findById(agenciaDTO.getBancoId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Banco não encontrado com ID: " + agenciaDTO.getBancoId()));

        Agencia agencia = new Agencia();
        agencia.setNumero(agenciaDTO.getNumero());
        agencia.setNome(agenciaDTO.getNome());
        agencia.setBanco(banco);

        return agenciaRepository.save(agencia);
    }

    public Agencia buscarPorId(Long id) {
        return agenciaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Agência não encontrada com ID: " + id));
    }

    public Agencia buscarPorNumero(Integer numero) {
        return agenciaRepository.findByNumero(numero)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Agência não encontrada com número: " + numero));
    }

    public List<Agencia> buscarPorBanco(Long bancoId) {
        if (!bancoRepository.existsById(bancoId)) {
            throw new EntidadeNaoEncontradaException("Banco não encontrado com ID: " + bancoId);
        }
        return agenciaRepository.findByBancoId(bancoId);
    }

    @Transactional
    public Agencia atualizar(Long id, AgenciaDTO agenciaDTO) {
        Agencia agenciaExistente = agenciaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Agência não encontrada com ID: " + id));

        Banco banco = bancoRepository.findById(agenciaDTO.getBancoId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Banco não encontrado com ID: " + agenciaDTO.getBancoId()));

        agenciaExistente.setNumero(agenciaDTO.getNumero());
        agenciaExistente.setNome(agenciaDTO.getNome());
        agenciaExistente.setBanco(banco);

        return agenciaRepository.save(agenciaExistente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!agenciaRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Agência não encontrada com ID: " + id);
        }
        agenciaRepository.deleteById(id);
    }
}