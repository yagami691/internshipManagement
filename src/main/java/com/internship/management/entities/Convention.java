package com.internship.management.entities;

import jakarta.persistence.*;

import java.io.File;

public class Convention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private File pdfConvention;
    private String state;


    @OneToOne
    @JoinColumn(name = "application_id", unique = true)
    private Application application;


    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;
}
