package com.internship.management.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Enterprise extends Users {

    private String sector;
    private String matriculation;

    @OneToMany(mappedBy = "enterprise", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Offer> offers = new ArrayList<>();

}
