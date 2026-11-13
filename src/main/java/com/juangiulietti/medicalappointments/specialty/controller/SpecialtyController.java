package com.juangiulietti.medicalappointments.specialty.controller;

import com.juangiulietti.medicalappointments.specialty.dto.SpecialtyRequest;
import com.juangiulietti.medicalappointments.specialty.dto.SpecialtyResponse;
import com.juangiulietti.medicalappointments.specialty.service.SpecialtyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtyController {
    private final SpecialtyService service;

    public SpecialtyController(SpecialtyService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SpecialtyResponse createSpecialty(@RequestBody @Valid SpecialtyRequest request) {
        return service.createSpecialty(request);
    }

    @GetMapping
    public List<SpecialtyResponse> getAllSpecialities() {
        return service.getAllSpecialities();
    }

    @GetMapping("/{id}")
    public SpecialtyResponse getSpecialtyById(@PathVariable Long id){
        return service.getSpecialtyById(id);
    }

    @PatchMapping("/{id}")
    public SpecialtyResponse updateSpecialty(@RequestBody @Valid SpecialtyRequest request, @PathVariable Long id){
        return service.updateSpecialty(request, id);
    }

    @DeleteMapping("/{id}")
    public void deleteSpecialty(@PathVariable Long id){
        service.deleteSpecialty(id);
    }
}
