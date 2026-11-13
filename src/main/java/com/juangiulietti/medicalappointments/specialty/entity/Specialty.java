package com.juangiulietti.medicalappointments.specialty.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "specialty")
public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialty_id")
    private Long id;

    @Column(nullable = false , unique = true, length = 100)
    private String name;

    protected Specialty() {}
    public Specialty(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void rename(String name){
        this.name = name;
    }
}
