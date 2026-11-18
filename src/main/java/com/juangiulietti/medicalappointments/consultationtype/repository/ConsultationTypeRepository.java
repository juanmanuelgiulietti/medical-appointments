package com.juangiulietti.medicalappointments.consultationtype.repository;

import com.juangiulietti.medicalappointments.consultationtype.entity.ConsultationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsultationTypeRepository extends JpaRepository<ConsultationType, Long> {
    boolean existsBySpecialtyIdAndName(Long specialtyId, String name);
    boolean existsBySpecialtyIdAndNameAndIdNot(Long specialtyId, String name, Long id);
    List<ConsultationType> findAllBySpecialtyId(Long specialtyId);
    Optional<ConsultationType> findBySpecialtyIdAndId(Long specialtyId, Long id);
}
