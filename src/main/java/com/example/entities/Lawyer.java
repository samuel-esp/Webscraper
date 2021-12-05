package com.example.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Lawyer {

    private String id;

    private String name;

    private Double yearsOfExperience;

    private List<String> specialization;

    private List<String> statesOfAbilitation;

    private String website;

    private Set<LawyerOffice> lawyerOfficeSet;

}
