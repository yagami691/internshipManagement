package com.internship.management.repositories;

import com.internship.management.entities.Convention;
import com.internship.management.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConventionRepository extends JpaRepository<Convention,Long> {

    boolean existsByOffer(Offer offer);
}
