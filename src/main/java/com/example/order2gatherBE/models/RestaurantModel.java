package com.example.order2gatherBE.models;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RestaurantModel {

    private Integer id;
    @NotNull
    private Integer uid;
    @NotNull
    private String name;
    @NotNull
    private Boolean isDelete;

    private String address;

    @Size(min=8, max=10)
    @Pattern(regexp = "[0-9]+", message = "Phone number is wrong.")
    private String phone;

    private String openHour;

}
