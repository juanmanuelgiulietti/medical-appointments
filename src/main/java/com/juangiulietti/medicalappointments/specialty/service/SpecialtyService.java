package com.juangiulietti.medicalappointments.specialty.service;

import com.juangiulietti.medicalappointments.exception.DuplicateException;
import com.juangiulietti.medicalappointments.exception.NotFoundException;
import com.juangiulietti.medicalappointments.specialty.dto.SpecialtyRequest;
import com.juangiulietti.medicalappointments.specialty.dto.SpecialtyResponse;
import com.juangiulietti.medicalappointments.specialty.entity.Specialty;
import com.juangiulietti.medicalappointments.specialty.mapper.SpecialtyMapper;
import com.juangiulietti.medicalappointments.specialty.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpecialtyService {
    private final SpecialtyRepository repository;

    public SpecialtyService(SpecialtyRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public SpecialtyResponse createSpecialty(SpecialtyRequest request) {
        String name = request.name().strip();

        if (repository.existsByName(name)) {
            throw new DuplicateException("Specialty " + name + " already exists");
        }

        Specialty specialty = new Specialty(name);
        Specialty saved = repository.save(specialty);

        return SpecialtyMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SpecialtyResponse> getAllSpecialities() {
        return repository.findAll().stream()
                .map(SpecialtyMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SpecialtyResponse getSpecialtyById(Long id){
        Specialty specialty = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Specialty " + id + " not found"));

        return SpecialtyMapper.toResponse(specialty);
    }

    @Transactional
    public SpecialtyResponse updateSpecialty(SpecialtyRequest request, Long id){
        String name = request.name().strip();

        Specialty specialty = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Specialty " + id + " not found"));

        if(repository.existsByNameAndIdNot(name, id)) {
            throw new DuplicateException("Specialty " + name + " already exists");
        }

        specialty.rename(name);

        return SpecialtyMapper.toResponse(specialty);
    }

    @Transactional
    public void deleteSpecialty(Long id){
        Specialty specialty = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Specialty " + id + " not found"));

        repository.delete(specialty);
    }
}
