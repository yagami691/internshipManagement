package com.internship.management.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Entity
@Getter
@Setter
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String state;
    private File cv;
    private File coverLetter;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

//    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Convention convention;
//
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "offer_id", nullable = false)
//    private Offer offer;
}
