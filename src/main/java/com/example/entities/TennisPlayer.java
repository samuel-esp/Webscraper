package com.example.entities;

import lombok.*;

@Data
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TennisPlayer {

    private String id;

    private String name;

    private String surname;

    private String careerHighRanking;

    private Integer age;

    private String birthplace;

    private String residence;

    private String staff;

    private String styleOfPlay;

    private Integer turnedPro;

    private Integer weight;

    private Integer height;

    private Integer titlesCount;

    private Integer wonGamesCount;

    private Integer lostGamesCount;

    private Double prizeMoney;


}
