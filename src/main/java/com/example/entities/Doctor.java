package com.example.entities;

import lombok.*;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString
public class Doctor {

    private String id;

    private String name;

    private String NPINumber;

    private List<String> specialization;

    private List<String> spokenLanguages;

    private String address;

    /*
    private String city;

    private String state;

    private String postalCode;
    */

    private Double rating;



}
