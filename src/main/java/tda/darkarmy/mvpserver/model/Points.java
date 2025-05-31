package tda.darkarmy.mvpserver.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "points") // MongoDB collection name
public class Points {

    @Id
    private String id; // MongoDB uses String (ObjectId)

    @Field("user_id") // Store user ID instead of full object
    private String userId;

    @Field("total_points")
    private int totalPoints;
}
