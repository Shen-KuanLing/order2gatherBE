package com.example.order2gatherBE.models;

import javax.validation.constraints.NotNull;
import lombok.Data;

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
