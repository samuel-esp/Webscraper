package com.example.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class IndeedCompany {

    private String id;

    private String name;

    private String headquarters;

    private String employeesSize;

    private String industryType;

    private List<String> websiteList;

    private String happinessScore;

    private Double reviewScore;

    private Integer jobOffersCount;


}
