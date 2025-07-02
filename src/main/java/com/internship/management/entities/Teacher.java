package com.internship.management.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Teacher extends User{

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Convention> conventions = new ArrayList<>();
}
