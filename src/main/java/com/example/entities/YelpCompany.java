package com.example.entities;
import lombok.*;
import java.util.Date;

@Data
@AllArgsConstructor @NoArgsConstructor
@ToString
public class YelpCompany {

    private String id;

    private String name;

    private String type;

    private Double starsCount;

    private Integer reviewsCount;

    private String mobileNumber;

    private String address;

    private String website;

    private String workingHours;


}
