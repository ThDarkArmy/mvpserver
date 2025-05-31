package tda.darkarmy.mvpserver.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "properties")
public class Property {

    @Id
    private String id;

    private String name;
    private String location;
    private int price;
    private String builder;
    private String contact;

    @Field("images") // Renamed to "images" and changed type to List<String>
    private List<String> images; // List of image URLs or Base64-encoded strings

    private String description;

    @Field("features")
    private List<String> features;

    @Field("points_required")
    private int pointsRequired;

    private String discount;

    @Field("user_id")
    private String userId;
}
