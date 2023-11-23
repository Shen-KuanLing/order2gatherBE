package com.example.order2gatherBE.models;


import javax.validation.constraints.NotNull;
import lombok.*;

@Data
public class RestaurantImageModel {

    private byte[] image;

    @NotNull
    private Integer rId;


}
