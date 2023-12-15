package com.example.order2gatherBE.models;

import java.util.Base64;
import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantImageModel {
    private byte[] menuImage;

    @NotNull
    private Integer rId;

    public String toString() {
        String encoderImage = Base64.getEncoder().encodeToString(menuImage);
        return encoderImage;
    }
}
