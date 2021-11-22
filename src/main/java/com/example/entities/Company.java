package com.example.entities;
import lombok.*;
import java.util.Date;

@Data
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Company {

    private int id;

    private String companyName;

    private String companyType;

    private int review;

    private String mobile;

    private String address;

    private String website;

    private Date date;


}
