package com.juangiulietti.medicalappointments.location.controller;

import com.juangiulietti.medicalappointments.location.dto.LocationRequest;
import com.juangiulietti.medicalappointments.location.dto.LocationResponse;
import com.juangiulietti.medicalappointments.location.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationService service;

    public LocationController(LocationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationResponse createLocation(@RequestBody @Valid LocationRequest request) {
        return service.createLocation(request);
    }

    @GetMapping
    public List<LocationResponse> getAllLocations() {
        return service.getAllLocations();
    }

    @GetMapping("/{id}")
    public LocationResponse getLocationById(@PathVariable Long id){
        return service.getLocationById(id);
    }

    @PutMapping("/{id}")
    public LocationResponse updateLocation(@RequestBody @Valid LocationRequest request, @PathVariable Long id){
        return service.updateLocation(request, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocation(@PathVariable Long id){
        service.deleteLocation(id);
    }
}
