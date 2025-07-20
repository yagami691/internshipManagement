package com.internship.management.entities;

import com.internship.management.enums.ConventionState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.File;


@Entity
@Getter
@Setter
public class Convention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private File pdfConvention;
    private String state;

    @Enumerated(EnumType.STRING)
    private ConventionState conventionState;

//    @OneToOne
//    @JoinColumn(name = "application_id", unique = true)
//    private Application application;

    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher reviewer;

    @OneToOne
    @JoinColumn(name = "offer_id", unique = true)
    private Offer offer;
}
