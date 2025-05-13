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
import java.util.stream.Collectors;

@Service
public class AgenciaService {

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Autowired
    private BancoRepository bancoRepository;

    @Transactional
    public AgenciaDTO salvar(AgenciaDTO agenciaDTO) {
        Banco banco = bancoRepository.findById(agenciaDTO.getBancoId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Banco não encontrado com ID: " + agenciaDTO.getBancoId()));

        Agencia agencia = new Agencia(agenciaDTO.getNumero(), agenciaDTO.getNome(), banco);
        Agencia agenciaSalva = agenciaRepository.save(agencia);
        return converterParaDTO(agenciaSalva);
    }

    public AgenciaDTO buscarPorId(Long id) {
        Agencia agencia = agenciaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Agência não encontrada com ID: " + id));
        return converterParaDTO(agencia);
    }

    public AgenciaDTO buscarPorNumero(Integer numero) {
        Agencia agencia = agenciaRepository.findByNumero(numero)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Agência não encontrada com número: " + numero));
        return converterParaDTO(agencia);
    }

    public List<AgenciaDTO> buscarPorBanco(Long bancoId) {
        return agenciaRepository.findByBancoId(bancoId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletar(Long id) {
        if (!agenciaRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Agência não encontrada com ID: " + id);
        }
        agenciaRepository.deleteById(id);
    }

    @Transactional
    public AgenciaDTO atualizar(Long id, AgenciaDTO agenciaDTO) {
        Agencia agencia = agenciaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Agência não encontrada com ID: " + id));

        Banco banco = bancoRepository.findById(agenciaDTO.getBancoId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Banco não encontrado com ID: " + agenciaDTO.getBancoId()));

        agencia.setNumero(agenciaDTO.getNumero());
        agencia.setNome(agenciaDTO.getNome());
        agencia.setBanco(banco);

        return converterParaDTO(agenciaRepository.save(agencia));
    }

    private AgenciaDTO converterParaDTO(Agencia agencia) {
        return new AgenciaDTO(
                agencia.getId(),
                agencia.getNumero(),
                agencia.getNome(),
                agencia.getBanco().getId());
    }
}