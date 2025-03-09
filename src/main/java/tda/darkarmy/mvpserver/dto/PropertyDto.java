package tda.darkarmy.mvpserver.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDto {

    private String name;
    private String location;
    private int price;
    private String builder;
    private String contact;
    private MultipartFile image; // Multipart file for image upload
    private String description;
    private List<String> features;
    private int pointsRequired;
    private String discount;
    private Long userId;  // User ID for association
}

