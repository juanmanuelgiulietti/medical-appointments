package com.juangiulietti.medicalappointments.location.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LocationRequest(
        @NotBlank(message = "Name cannot be blank") @Size(max = 100, message = "Name length must be less than 100")
        String name,

        @NotBlank(message = "Address cannot be blank") @Size(max = 255, message = "Address length must be less than 255")
        String address
){}
