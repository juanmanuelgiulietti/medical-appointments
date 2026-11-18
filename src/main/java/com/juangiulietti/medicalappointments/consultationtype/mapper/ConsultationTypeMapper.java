package com.juangiulietti.medicalappointments.consultationtype.mapper;

import com.juangiulietti.medicalappointments.consultationtype.dto.ConsultationTypeResponse;
import com.juangiulietti.medicalappointments.consultationtype.entity.ConsultationType;

public class ConsultationTypeMapper {
    private ConsultationTypeMapper(){}

    public static ConsultationTypeResponse toResponse(ConsultationType consultationType) {
        return new ConsultationTypeResponse(
                consultationType.getId(),
                consultationType.getSpecialty().getId(),
                consultationType.getName(),
                consultationType.getDurationMinutes());
    }
}
