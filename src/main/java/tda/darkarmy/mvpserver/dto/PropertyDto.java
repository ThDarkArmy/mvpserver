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

    // Changed from MultipartFile to List<MultipartFile>
    private List<MultipartFile> images;

    private String description;
    private List<String> features;
    private int pointsRequired;
    private String discount;
}
