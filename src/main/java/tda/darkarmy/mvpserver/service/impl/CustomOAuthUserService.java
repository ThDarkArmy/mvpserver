package tda.darkarmy.mvpserver.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import tda.darkarmy.mvpserver.enums.Role;
import tda.darkarmy.mvpserver.model.User;
import tda.darkarmy.mvpserver.repository.UserRepository;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class CustomOAuthUserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        log.info("CustomOAuth2UserService initialized!"); // Should appear at startup
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        // 1. Load default user info from provider
        log.info("Load User");
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        // 2. Extract provider details
        String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 3. Build user from provider data
        String providerId = getProviderId(attributes, provider);
        String email = getEmail(attributes, provider);
        String name = getName(attributes, provider);

        // 4. Find or create user
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> registerNewUser(provider, providerId, email, name));

        // 5. Return Spring Security's OAuth2User
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                attributes,
                getPrimaryAttribute(provider)
        );
    }

    private User registerNewUser(String provider, String providerId, String email, String name) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
//        user.setProvider(Provider.valueOf(provider.toUpperCase()));
//        user.setProviderId(providerId);
        user.setRole(Role.USER); // Default role

        return userRepository.save(user);
    }

    // Helper methods to handle different providers' response formats
    private String getProviderId(Map<String, Object> attributes, String provider) {
        return switch (provider.toLowerCase()) {
            case "google" -> attributes.get("sub").toString();
            case "github" -> attributes.get("id").toString();
            case "facebook" -> attributes.get("id").toString();
            default -> throw new OAuth2AuthenticationException("Unsupported provider");
        };
    }

    private String getEmail(Map<String, Object> attributes, String provider) {
        // Handle different provider email fields
        return switch (provider.toLowerCase()) {
            case "google", "facebook" -> attributes.get("email").toString();
            case "github" -> (String) ((Map<?, ?>) attributes.get("email")).get("email");
            default -> throw new OAuth2AuthenticationException("Unsupported provider");
        };
    }

    private String getName(Map<String, Object> attributes, String provider) {
        return switch (provider.toLowerCase()) {
            case "google" -> attributes.get("name").toString();
            case "github" -> attributes.get("login").toString();
            case "facebook" -> attributes.get("name").toString();
            default -> throw new OAuth2AuthenticationException("Unsupported provider");
        };
    }

    private String getPrimaryAttribute(String provider) {
        return switch (provider.toLowerCase()) {
            case "google", "facebook" -> "email";
            case "github" -> "login";
            default -> throw new OAuth2AuthenticationException("Unsupported provider");
        };
    }
}
