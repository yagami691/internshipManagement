package com.internship.management.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class Student extends User{


    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();
}
