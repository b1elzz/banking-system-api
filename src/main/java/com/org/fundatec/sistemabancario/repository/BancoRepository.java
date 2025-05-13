package com.org.fundatec.sistemabancario.repository;

import com.org.fundatec.sistemabancario.model.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BancoRepository extends JpaRepository<Banco, Long> {
    Optional<Banco> findByCodigo(Integer codigo);
    List<Banco> findByNomeContainingIgnoreCase(String nome);
}