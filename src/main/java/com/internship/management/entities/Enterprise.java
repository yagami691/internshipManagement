package com.internship.management.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Enterprise extends Users {

    private String matriculation;
    private String contact;
    private String location;
    private String country;
    private boolean remote;
    private boolean paying;

    @OneToOne(mappedBy = "enterprise", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Logo logo;

    @OneToMany(mappedBy = "enterprise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Offer> offers = new ArrayList<>();

    @OneToMany(mappedBy ="enterprise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();
}
