package tda.darkarmy.mvpserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private int price;
    private String builder;
    private String contact;

    @Lob  // Store image as Base64 in DB
    private String image;

    private String description;

    @ElementCollection
    private List<String> features;

    private int pointsRequired;
    private String discount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
