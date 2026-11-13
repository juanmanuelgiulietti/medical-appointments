package com.juangiulietti.medicalappointments.specialty.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SpecialtyRequest(
        @NotBlank(message = "Name cannot be blank") @Size(max = 100, message = "Name length must be less than 100")
        String name
){}


