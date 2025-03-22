package tda.darkarmy.mvpserver.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "transactions") // Collection name in MongoDB
public class Transaction {

    @Id
    private String id;

    @DBRef // Reference to the User document (not a SQL join)
    private User user;

    @Field("bill_amount")
    private String billAmount;

    @Field("points_earned")
    private int pointsEarned;

    @Field("points_redeemed")
    private int pointsRedeemed;

    private String date;

    @Field("property_name")
    private String propertyName;

    private String status;

    @Field("brokerage_fee")
    private int brokerageFee = 0;
}
