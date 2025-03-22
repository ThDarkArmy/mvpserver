package tda.darkarmy.mvpserver.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import tda.darkarmy.mvpserver.enums.Role;
import tda.darkarmy.mvpserver.enums.SocialLoginProvider;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users") // MongoDB collection
public class User {

    @Id
    private String id;

    private String email;

    private String password;
    private String name;

    @Field("profile_picture_url")
    private String profilePictureUrl;

    @Field("phone_number")
    private String phoneNumber;

    private Role role;

    @Field("email_verified")
    private Boolean emailVerified = false;

    @CreatedDate // Automatically sets creation date
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate // Automatically updates on save
    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("points_balance")
    private Integer pointsBalance = 0;

    @Field("reset_token")
    private String resetToken;

    @Field("social_login_provider")
    private SocialLoginProvider socialLoginProvider;

    @Field("social_login_id")
    private String socialLoginId;

    @Field("is_active")
    private Boolean isActive = true;

    @Field("last_login")
    private LocalDateTime lastLogin;

    @Field("address")
    private String address;

    private Long otp;
    private String newPassword;
}
