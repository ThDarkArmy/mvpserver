package tda.darkarmy.mvpserver.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "invoice_details") // Defines MongoDB collection
public class InvoiceDetails {

    @Id
    private String id; // MongoDB uses String as ObjectId

    @Field("invoice_number")
    private String invoiceNumber;

    @Field("invoice_date")
    private String invoiceDate;

    private String seller;
    private String address;
    private String amount;
    private String status;
}
