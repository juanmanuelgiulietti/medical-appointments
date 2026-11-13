package com.juangiulietti.medicalappointments.specialty.mapper;

import com.juangiulietti.medicalappointments.specialty.dto.SpecialtyResponse;
import com.juangiulietti.medicalappointments.specialty.entity.Specialty;

public class SpecialtyMapper {
    private SpecialtyMapper() {}

    public static SpecialtyResponse toResponse(Specialty specialty) {
        return new SpecialtyResponse(
                specialty.getId(),
                specialty.getName());
    }
}
