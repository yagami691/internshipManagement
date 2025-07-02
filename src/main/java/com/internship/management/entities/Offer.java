package com.internship.management.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String title;
    private String description;
    private String domain;

    @ManyToOne
    @JoinColumn(name = "enterprise_id", nullable = false)
    private Enterprise enterprise;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL)
    private List<Application> applications = new ArrayList<>();

}
