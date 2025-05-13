package com.org.fundatec.sistemabancario.repository;

import com.org.fundatec.sistemabancario.model.Agencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgenciaRepository extends JpaRepository<Agencia, Long> {
    Optional<Agencia> findByNumero(Integer numero);
    List<Agencia> findByBancoId(Long bancoId);
}