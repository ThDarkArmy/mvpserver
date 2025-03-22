package tda.darkarmy.mvpserver.service;

import jakarta.mail.MessagingException;
import tda.darkarmy.mvpserver.dto.LoginRequest;
import tda.darkarmy.mvpserver.dto.LoginResponse;
import tda.darkarmy.mvpserver.dto.PasswordResetRequest;
import tda.darkarmy.mvpserver.dto.UserDto;
import tda.darkarmy.mvpserver.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Get a user by email
    Optional<User> getUserByEmail(String email);

    // Get a user by ID
    Optional<User> getUserById(String id);

    // Update a user's details
    User updateUser(UserDto userDto, String id);

    // Delete a user by ID
    String deleteUser();

    // Check if an email is already registered
    boolean isEmailRegistered(String email);

    // Get a user by reset token
    User getUserByResetToken(String token);

    User signup(UserDto userDto) throws MessagingException;

    String verifyOtp(String email, Long otp);

    String verifyOtpPassword(String email, Long otp);

    LoginResponse login(LoginRequest loginRequest);

    User changePassword(PasswordResetRequest request) throws MessagingException;

    String deleteById(String id);

    List<User> findAll();

    User getLoggedInUser();
}
