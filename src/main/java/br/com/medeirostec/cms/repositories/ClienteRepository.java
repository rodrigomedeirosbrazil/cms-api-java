package br.com.medeirostec.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medeirostec.cms.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}