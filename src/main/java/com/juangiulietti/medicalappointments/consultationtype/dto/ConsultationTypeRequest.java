package com.juangiulietti.medicalappointments.consultationtype.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ConsultationTypeRequest(
        @NotBlank(message = "Name cannot be blank") @Size(max = 100, message = "Name length must be at most 100")
        String name,

        @NotNull(message = "Duration minutes cannot be null")
        @Positive(message = "Duration minutes must be greater than 0")
        Integer durationMinutes
) {}
