package tda.darkarmy.mvpserver.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "notifications") // Defines MongoDB collection
public class Notifications {

    @Id
    private String id; // MongoDB uses String as ObjectId

    @Field("user_id")
    private String userId; // Store user ID as String (MongoDB ObjectId)

    private String title;
    private String message;

    @Field("is_read")
    private boolean isRead;

    @Field("created_at")
    private LocalDateTime createdAt;
}
