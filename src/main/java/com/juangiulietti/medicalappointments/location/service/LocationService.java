package com.juangiulietti.medicalappointments.location.service;

import com.juangiulietti.medicalappointments.exception.DuplicateException;
import com.juangiulietti.medicalappointments.exception.NotFoundException;
import com.juangiulietti.medicalappointments.location.dto.LocationRequest;
import com.juangiulietti.medicalappointments.location.dto.LocationResponse;
import com.juangiulietti.medicalappointments.location.entity.Location;
import com.juangiulietti.medicalappointments.location.mapper.LocationMapper;
import com.juangiulietti.medicalappointments.location.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationService {
    private final LocationRepository repository;

    public LocationService(LocationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public LocationResponse createLocation(LocationRequest request){
        String name = request.name().strip();
        String address = request.address().strip();

        if (repository.existsByName(name)) {
            throw new DuplicateException("Location " + name + " already exists");
        }

        Location location = new Location(name, address);
        Location saved = repository.save(location);

        return LocationMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<LocationResponse> getAllLocations() {
        return repository.findAll().stream()
                .map(LocationMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LocationResponse getLocationById(Long id){
        Location location = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Location " + id + " not found"));

        return LocationMapper.toResponse(location);
    }

    @Transactional
    public LocationResponse updateLocation(LocationRequest request, Long id){
        String name = request.name().strip();
        String address = request.address().strip();

        Location location = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Location " + id + " not found"));

        if(repository.existsByNameAndIdNot(name, id)) {
            throw new DuplicateException("Location " + name + " already exists");
        }

        location.update(name, address);

        return LocationMapper.toResponse(location);
    }

    @Transactional
    public void deleteLocation(Long id){
        Location location = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Location " + id + " not found"));

        repository.delete(location);
    }
}
