package com.internship.management.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.File;

public class Convention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private File pdfConvention;
    private String state;
    private Teacher teacher;
    private Application application;
}
