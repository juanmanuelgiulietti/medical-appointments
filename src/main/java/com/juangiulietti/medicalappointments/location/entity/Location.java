package com.juangiulietti.medicalappointments.location.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String address;

    protected Location(){}
    public Location(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void update(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
