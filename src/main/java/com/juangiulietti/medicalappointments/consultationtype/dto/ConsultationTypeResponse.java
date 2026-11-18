package com.juangiulietti.medicalappointments.consultationtype.dto;

public record ConsultationTypeResponse (
        Long id,
        Long specialtyId,
        String name,
        Integer durationMinutes
){}
