package com.internship.management.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.File;

public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String state;
    private File cv;
    private File ooverLetter;
    private Student student;
    private Offer offer;
}
