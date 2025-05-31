package tda.darkarmy.mvpserver.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oauth2User;
    @Getter
    private final String registrationId;
    private final String name;
    @Getter
    private final String email;

    public CustomOAuth2User(OAuth2User oauth2User, String registrationId) {
        this.oauth2User = oauth2User;
        this.registrationId = registrationId;
        this.name = extractName(oauth2User, registrationId);
        this.email = extractEmail(oauth2User, registrationId);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return this.name;
    }

    private String extractName(OAuth2User oauth2User, String registrationId) {
        return switch (registrationId) {
            case "google" -> oauth2User.getAttribute("name");
            case "github" -> oauth2User.getAttribute("login");
            case "facebook" -> oauth2User.getAttribute("name");
            default -> oauth2User.getName();
        };
    }

    private String extractEmail(OAuth2User oauth2User, String registrationId) {
        return switch (registrationId) {
            case "google" -> oauth2User.getAttribute("email");
            case "github" -> oauth2User.getAttribute("email") != null ?
                    oauth2User.getAttribute("email") :
                    oauth2User.getAttribute("login") + "@users.noreply.github.com";
            case "facebook" -> oauth2User.getAttribute("email");
            default -> null;
        };
    }

    // Builder pattern for easier construction
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private OAuth2User oauth2User;
        private String registrationId;

        public Builder oauth2User(OAuth2User oauth2User) {
            this.oauth2User = oauth2User;
            return this;
        }

        public Builder registrationId(String registrationId) {
            this.registrationId = registrationId;
            return this;
        }

        public CustomOAuth2User build() {
            return new CustomOAuth2User(oauth2User, registrationId);
        }
    }
}
