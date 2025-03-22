package tda.darkarmy.mvpserver.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "properties") // Defines MongoDB collection
public class Property {

    @Id
    private String id; // MongoDB uses String for ObjectId

    private String name;
    private String location;
    private int price;
    private String builder;
    private String contact;

    @Field("image") // Storing Base64 image as a String
    private String image;

    private String description;

    @Field("features")
    private List<String> features;

    @Field("points_required")
    private int pointsRequired;

    private String discount;

    @Field("user_id") // Storing only the User ID (Recommended)
    private String userId;
}
