package com.juangiulietti.medicalappointments.specialty.repository;

import com.juangiulietti.medicalappointments.specialty.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
