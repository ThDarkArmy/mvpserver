package tda.darkarmy.mvpserver.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tda.darkarmy.mvpserver.enums.Role;
import tda.darkarmy.mvpserver.enums.SocialLoginProvider;
import tda.darkarmy.mvpserver.model.User;
import tda.darkarmy.mvpserver.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    @Autowired
    private OAuth2AuthorizedClientService clientService;

    @Value("client_base_url")
    private String CLIENT_BASE_URL;

    public OAuth2LoginSuccessHandler(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
//        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
//        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
//                token.getAuthorizedClientRegistrationId(),
//                token.getName()
//        );
//
//        String accessToken = client.getAccessToken().getTokenValue();

        log.info("Oauth2User12: "+ authentication.toString());
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        log.info("Oauth2User11: "+ oauthUser.getAuthorities().toString());
        log.info("Oauth2User13: "+ oauthUser.getAttributes().toString());
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();

        String email = (String) oauthUser.getAttributes().get("email");
        String name = (String) oauthUser.getAttributes().get("name");
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = null;
        if(userOptional.isEmpty()){
            user = User.builder()
                    .email(email)
                    .name(name)
                    .emailVerified(true)
                    .pointsBalance(5000)
                    .address("")
                    .role(Role.USER)
                    .socialLoginProvider(SocialLoginProvider.GOOGLE)
                    .password("password")
                    .build();
            user = userRepository.save(user);
        }else{
            user = userOptional.get();
        }
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
        String token = jwtTokenProvider.generateTokenWithAuthorities(email, grantedAuthorityList);

        String redirectUrl = "https://first-buy.com?token=" + token +"&name="+user.getName()+"&role="+user.getRole().name()+"&id="+user.getId();

        response.sendRedirect(redirectUrl);
//        LoginResponse loginResponse = LoginResponse.builder().token(token).user(user).build();
//        response.setContentType("application/json");
//        new ObjectMapper().writeValue(response.getWriter(), loginResponse);
    }
}