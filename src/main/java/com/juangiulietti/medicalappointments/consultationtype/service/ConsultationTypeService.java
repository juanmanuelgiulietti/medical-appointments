package com.juangiulietti.medicalappointments.consultationtype.service;

import com.juangiulietti.medicalappointments.consultationtype.dto.ConsultationTypeRequest;
import com.juangiulietti.medicalappointments.consultationtype.dto.ConsultationTypeResponse;
import com.juangiulietti.medicalappointments.consultationtype.entity.ConsultationType;
import com.juangiulietti.medicalappointments.consultationtype.mapper.ConsultationTypeMapper;
import com.juangiulietti.medicalappointments.consultationtype.repository.ConsultationTypeRepository;
import com.juangiulietti.medicalappointments.exception.DuplicateException;
import com.juangiulietti.medicalappointments.exception.NotFoundException;
import com.juangiulietti.medicalappointments.specialty.entity.Specialty;
import com.juangiulietti.medicalappointments.specialty.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConsultationTypeService {
    private final ConsultationTypeRepository repository;
    private final SpecialtyRepository specialtyRepository;

    public ConsultationTypeService(ConsultationTypeRepository repository, SpecialtyRepository specialtyRepository) {
        this.repository = repository;
        this.specialtyRepository = specialtyRepository;
    }

    @Transactional
    public ConsultationTypeResponse createConsultationType(Long specialtyId, ConsultationTypeRequest request){
        String name = request.name().strip();

        Specialty specialty = specialtyRepository.findById(specialtyId)
                .orElseThrow(() -> new NotFoundException("Specialty " + specialtyId + " not found"));

        if(repository.existsBySpecialtyIdAndName(specialtyId, name)){
            throw new DuplicateException("Consultation type " + name + " already exists in specialty " + specialtyId);
        }

        ConsultationType consultationType = new ConsultationType(specialty, name, request.durationMinutes());
        ConsultationType saved = repository.save(consultationType);

        return ConsultationTypeMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ConsultationTypeResponse> getAllConsultationTypes(Long specialtyId) {
        if (!specialtyRepository.existsById(specialtyId)) {
            throw new NotFoundException("Specialty " + specialtyId + " not found");
        }

        return repository.findAllBySpecialtyId(specialtyId).stream()
                .map(ConsultationTypeMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConsultationTypeResponse getConsultationTypeById(Long specialtyId, Long id){
        ConsultationType consultationType = repository.findBySpecialtyIdAndId(specialtyId, id)
                .orElseThrow(() -> new NotFoundException("Consultation type " + id + " not found in specialty " + specialtyId));

        return ConsultationTypeMapper.toResponse(consultationType);
    }

    @Transactional
    public ConsultationTypeResponse updateConsultationType(Long specialtyId, Long id, ConsultationTypeRequest request){
        String name = request.name().strip();

        ConsultationType consultationType = repository.findBySpecialtyIdAndId(specialtyId, id)
                .orElseThrow(() -> new NotFoundException("Consultation type " + id + " not found in specialty " + specialtyId));

        if(repository.existsBySpecialtyIdAndNameAndIdNot(specialtyId, name, id)){
            throw new DuplicateException("Consultation type " + name + " already exists in specialty " + specialtyId);
        }

        consultationType.update(name, request.durationMinutes());
        return ConsultationTypeMapper.toResponse(consultationType);
    }

    @Transactional
    public void deleteConsultationType(Long specialtyId, Long id){
        ConsultationType consultationType = repository.findBySpecialtyIdAndId(specialtyId, id)
                .orElseThrow(() -> new NotFoundException("Consultation type " + id + " not found in specialty " + specialtyId));

        repository.delete(consultationType);
    }
}
