package com.juangiulietti.medicalappointments.consultationtype.controller;

import com.juangiulietti.medicalappointments.consultationtype.dto.ConsultationTypeRequest;
import com.juangiulietti.medicalappointments.consultationtype.dto.ConsultationTypeResponse;
import com.juangiulietti.medicalappointments.consultationtype.service.ConsultationTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialties/{specialtyId}/consultation-types")
public class ConsultationTypeController {
    private final ConsultationTypeService service;

    public ConsultationTypeController(ConsultationTypeService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultationTypeResponse createConsultationType(@PathVariable Long specialtyId, @RequestBody @Valid ConsultationTypeRequest request) {
        return service.createConsultationType(specialtyId, request);
    }

    @GetMapping
    public List<ConsultationTypeResponse> getAllConsultationTypes(@PathVariable Long specialtyId) {
        return service.getAllConsultationTypes(specialtyId);
    }

    @GetMapping("/{id}")
    public ConsultationTypeResponse getConsultationTypeById(@PathVariable Long specialtyId, @PathVariable Long id) {
        return service.getConsultationTypeById(specialtyId, id);
    }

    @PutMapping("/{id}")
    public ConsultationTypeResponse updateConsultationType(@PathVariable Long specialtyId, @PathVariable Long id, @RequestBody @Valid ConsultationTypeRequest request) {
        return service.updateConsultationType(specialtyId, id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConsultationType(@PathVariable Long specialtyId, @PathVariable Long id) {
        service.deleteConsultationType(specialtyId, id);
    }
}