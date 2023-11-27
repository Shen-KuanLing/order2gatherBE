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
        System.out.println(menuImage.length);
        String encoderImage = Base64.getEncoder().encodeToString(menuImage);

        return String.format(
            "{\"rid\":%s, \"menuImage\": \"%s\"}",
            rId,
            encoderImage
        );
    }
}
