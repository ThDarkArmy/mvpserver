package tda.darkarmy.mvpserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tda.darkarmy.mvpserver.model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    // Find user by email
    Optional<User> findByEmail(String email);

    // Find user by reset token
    Optional<User> findByResetToken(String resetToken);

    // Check if an email exists in the database
    boolean existsByEmail(String email);
}
