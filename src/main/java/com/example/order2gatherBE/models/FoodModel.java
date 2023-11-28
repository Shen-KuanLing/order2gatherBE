package com.example.order2gatherBE.models;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FoodModel {

    private Integer id;

    @NotNull
    private Integer rid;

    @NotNull
    private String name;

    @NotNull
    private Integer price;
}
