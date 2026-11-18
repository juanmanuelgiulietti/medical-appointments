package com.juangiulietti.medicalappointments.consultationtype.entity;

import com.juangiulietti.medicalappointments.specialty.entity.Specialty;
import jakarta.persistence.*;

@Entity
@Table(name = "consultation_type")
public class ConsultationType{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultation_type_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer durationMinutes;

    protected ConsultationType(){}
    public ConsultationType(Specialty specialty, String name, Integer durationMinutes){
        this.specialty = specialty;
        this.name = name;
        this.durationMinutes = durationMinutes;
    }

    public Long getId() {
        return id;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public String getName() {
        return name;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void update(String name, Integer durationMinutes){
        this.name = name;
        this.durationMinutes = durationMinutes;
    }
}
