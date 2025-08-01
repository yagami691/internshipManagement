package com.internship.management.repositories;

import com.internship.management.entities.Application;
import com.internship.management.entities.Offer;
import com.internship.management.enums.ApplicationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> {

    List<Application> findAllByEnterpriseId(Long id);

    @Query("SELECT a FROM Application a WHERE (a.state = 'APPROVED' OR a.state = 'REJECTED') AND a.student.id = :studentId")
    List<Application> findApprovedOrRejectedApplicationsByStudentId(@Param("studentId") Long studentId);


}
