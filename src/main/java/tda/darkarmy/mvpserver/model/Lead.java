package tda.darkarmy.mvpserver.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "leads") // Defines MongoDB collection
public class Lead {

    @Id
    private String id; // MongoDB uses String as ObjectId

    @Field("user_id")
    private String userId; // Store user ID as String (MongoDB ObjectId)

    @Field("property_id")
    private String propertyId; // Store property ID as String (MongoDB ObjectId)
}
