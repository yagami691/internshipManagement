package com.internship.management.repositories;

import com.internship.management.entities.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise,Long> {

    Optional<Enterprise> findByEmail(String email);
}
