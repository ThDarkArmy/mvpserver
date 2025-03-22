package tda.darkarmy.mvpserver.service.impl;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tda.darkarmy.mvpserver.config.JwtTokenProvider;
import tda.darkarmy.mvpserver.dto.LoginRequest;
import tda.darkarmy.mvpserver.dto.LoginResponse;
import tda.darkarmy.mvpserver.dto.PasswordResetRequest;
import tda.darkarmy.mvpserver.dto.UserDto;
import tda.darkarmy.mvpserver.enums.Role;
import tda.darkarmy.mvpserver.exception.ResourceNotFoundException;
import tda.darkarmy.mvpserver.exception.UserAlreadyExistsException;
import tda.darkarmy.mvpserver.model.Transaction;
import tda.darkarmy.mvpserver.model.User;
import tda.darkarmy.mvpserver.repository.TransactionRepository;
import tda.darkarmy.mvpserver.repository.UserRepository;
import tda.darkarmy.mvpserver.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static tda.darkarmy.mvpserver.mapper.UserMapper.toUserEntity;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private TransactionRepository transactionRepository;

    public User signup(UserDto userDto) throws MessagingException {

        Optional<User> userOptional = userRepository.findByEmail(userDto.getEmail());
        if(userOptional.isPresent() && userOptional.get().getEmailVerified()) throw new UserAlreadyExistsException("User with given email already exists");

        if(userOptional.isPresent()) userRepository.deleteById(userOptional.get().getId());
        User user = toUserEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.valueOf(userDto.getRole()));
        System.out.println("UserDto"+ userDto+" "+ user);
        Long otp = new Random().nextLong(9999 - 1000 + 1) + 1000;
        user.setOtp(otp);

        mailSenderService.send(user, otp.toString());
        user = userRepository.save(user);
        Transaction transaction = new Transaction();
        transaction.setPointsEarned(5000);
        transaction.setUser(user);
        transaction.setStatus("Active");
        transaction.setDate(LocalDateTime.now().toString());
        transaction.setPropertyName("");
        transaction.setPointsRedeemed(0);
        transaction.setBillAmount("0");
        transaction.setBrokerageFee(0);
        transactionRepository.save(transaction);
        return user;
    }

    public LoginResponse login(LoginRequest loginRequest){
        Optional<User> userOptional =  userRepository.findByEmail(loginRequest.getEmail());

        if(!userOptional.isPresent()) throw new ResourceNotFoundException("User with given email not found.");
        if(!userOptional.get().getEmailVerified()) throw new ResourceNotFoundException("User is not verified yet.");

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return new LoginResponse(token, userOptional.get());
    }

    public String verifyOtp(String email, Long otp){
        Optional<User> userOptional =  userRepository.findByEmail(email);
        User user = userOptional.get();
        if(user.getOtp().longValue()==otp.longValue()){
            user.setEmailVerified(true);
            userRepository.save(user);
            return "Otp verified successfully";
        }else{
            return "Invalid otp";
        }
    }


    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User updateUser(UserDto userDto, String id){
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found"));

        user = toUserEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }

    public User changePassword(PasswordResetRequest resetRequest) throws MessagingException {

        Optional<User> userOptional = userRepository.findByEmail(resetRequest.getEmail());
        if(userOptional.isEmpty()){
            throw new ResourceNotFoundException("No user found with given email.");
        }
        User user = userOptional.get();
        Long otp = new Random().nextLong(9999 - 1000 + 1) + 1000;
        user.setOtp(otp);

        mailSenderService.send(user, otp.toString());
        user.setNewPassword(passwordEncoder.encode(resetRequest.getPassword()));
        user.setPassword(passwordEncoder.encode(resetRequest.getPassword()));
        return userRepository.save(user);
    }

    public String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername(); // Return username from UserDetails
            } else if (principal instanceof String) {
                return (String) principal; // Principal is a String
            }
        }
        throw new IllegalStateException("No authenticated user found");
    }

    public User getLoggedInUser(){
        return userRepository.findByEmail(getLoggedInUsername()).get();
    }

    public User getById(String id){
        return userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }

    public Optional<User> getUser(String userId){
        return userRepository.findById(userId);
    }

    public String deleteUser(){
        User user = getLoggedInUser();
        userRepository.deleteById(user.getId());

        return "User deleted successfully";
    }

    @Override
    public boolean isEmailRegistered(String email) {
        return false;
    }

    @Override
    public User getUserByResetToken(String token) {
        return null;
    }

    public String deleteById(String id) {
        if(getUser(id).isEmpty()) throw new ResourceNotFoundException("User not found");
        userRepository.deleteById(id);
        return "User deleted successfully";
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String deleteAllUsers() {
        userRepository.deleteAll();
        return "All users deleted successfully";
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public String verifyOtpPassword(String email, Long otp) {
        Optional<User> userOptional =  userRepository.findByEmail(email);
        User user = userOptional.get();
        if(user.getOtp().longValue()==otp.longValue()){
            user.setPassword(user.getNewPassword());
            userRepository.save(user);
            return "Otp verified successfully";
        }else{
            return "Invalid otp";
        }
    }
}
