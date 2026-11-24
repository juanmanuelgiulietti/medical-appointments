package com.juangiulietti.medicalappointments.user.result;

import com.juangiulietti.medicalappointments.user.entity.User;

public record CreatedUser(
        User user,
        String initialPassword
) {}