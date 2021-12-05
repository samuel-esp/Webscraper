package com.example.entities;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString @EqualsAndHashCode
public class LawyerOffice {

    private String city;

    private String address;

    private String state;

    private String postalcode;

    private String telephone;

}
