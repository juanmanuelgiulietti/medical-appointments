package com.juangiulietti.medicalappointments.location.mapper;

import com.juangiulietti.medicalappointments.location.dto.LocationResponse;
import com.juangiulietti.medicalappointments.location.entity.Location;

public class LocationMapper {
    private LocationMapper(){}

    public static LocationResponse toResponse(Location location) {
        return new LocationResponse(
                location.getId(),
                location.getName(),
                location.getAddress());
    }
}
