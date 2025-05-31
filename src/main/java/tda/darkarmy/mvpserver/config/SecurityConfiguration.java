package tda.darkarmy.mvpserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tda.darkarmy.mvpserver.service.impl.CustomOAuth2UserService;

import java.util.Arrays;
import java.util.Map;

@Configuration
public class SecurityConfiguration {

	private final JwtRequestFilter jwtRequestFilter;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	private final AuthenticationProvider authenticationProvider;

	public SecurityConfiguration(JwtRequestFilter jwtRequestFilter,
						  CustomOAuth2UserService customOAuth2UserService,
						  OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler, AuthenticationProvider authenticationProvider) {
		this.jwtRequestFilter = jwtRequestFilter;
		this.customOAuth2UserService = customOAuth2UserService;
		this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
		this.authenticationProvider = authenticationProvider;
	}

    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.
		csrf(csrf -> csrf
				.ignoringRequestMatchers("/api/**", "/oauth2/**")
		)
				// Enable CORS
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))

				// Set session management to stateless
				.sessionManagement(session -> session
						.sessionFixation().changeSessionId()
				)

				// Configure authorization
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								new AntPathRequestMatcher("/api/auth/**"),
								new AntPathRequestMatcher("/.well-known/pki-validation/AA1EB99B81955FF9C9299CA98353416B.txt"),
								new AntPathRequestMatcher("/api/v1/users/login"),
								new AntPathRequestMatcher("/api/v1/users/signup"),
								new AntPathRequestMatcher("/health-check"),
								new AntPathRequestMatcher("/oauth2/**")
						).permitAll()
						.anyRequest().authenticated()
				)
				.authenticationProvider(authenticationProvider)
				// Configure OAuth2 login
				.oauth2Login(oauth2 -> oauth2
						.authorizationEndpoint(auth -> auth
								.baseUri("/oauth2/authorize")
						)
						.userInfoEndpoint(userInfo -> userInfo
								.userService(customOAuth2UserService)
						)
						.successHandler(oAuth2LoginSuccessHandler)
						.failureHandler((request, response, exception) -> {
							response.setStatus(HttpStatus.UNAUTHORIZED.value());
							response.setContentType("application/json");
							new ObjectMapper().writeValue(response.getWriter(),
									Map.of("error", "OAuth2 login failed"));
						})
				)

				// Add JWT filter
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}



	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOriginPattern("*");
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
		configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
