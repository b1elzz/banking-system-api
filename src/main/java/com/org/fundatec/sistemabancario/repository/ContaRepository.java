package com.org.fundatec.sistemabancario.repository;

import com.org.fundatec.sistemabancario.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    Optional<Conta> findByNumero(Integer numero);
}