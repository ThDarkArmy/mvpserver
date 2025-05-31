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
								new AntPathRequestMatcher("/api/v1/users/login"),
								new AntPathRequestMatcher("/api/v1/users/signup"),
								new AntPathRequestMatcher("/health-check"),
								new AntPathRequestMatcher("/oauth2/**")
						).permitAll()
						.anyRequest().authenticated()
				)
				.authenticationProvider(authenticationProvider)


				// Configure form login
//				.formLogin(form -> form
//						.loginProcessingUrl("/api/auth/login")
//						.successHandler((request, response, authentication) -> {
//							String token = jwtTokenProvider.generateToken(authentication);
//							response.setContentType("application/json");
//							new ObjectMapper().writeValue(response.getWriter(),
//									Map.of("token", token, "username", authentication.getName()));
//						})
//						.failureHandler((request, response, exception) -> {
//							response.setStatus(HttpStatus.UNAUTHORIZED.value());
//							response.setContentType("application/json");
//							new ObjectMapper().writeValue(response.getWriter(),
//									Map.of("error", "Login failed"));
//						})
//				)

				// Configure OAuth2 login
				.oauth2Login(oauth2 -> oauth2
						.authorizationEndpoint(auth -> auth
								.baseUri("/oauth2/authorize")
						)
//						.redirectionEndpoint(redirect -> redirect
//								.baseUri("/oauth2/callback/*")
//						)
//						http://localhost:8080/oauth2/callback/google
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

//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		return http
//				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
//				.csrf(csrf -> csrf
//						.ignoringRequestMatchers("/login/oauth2/code/**", "/api/v1/users/**")
//				)
//				.authorizeHttpRequests(auth -> auth
//						.requestMatchers(
//								"/oauth2/**",
//								"/login/oauth2/code/**",
//								"/api/v1/users/login",
//								"/api/v1/users/signup",
//								"/.well-known/**"
//						).permitAll()
//						.anyRequest().authenticated()
//				)
//				.authenticationProvider(authenticationProvider) // ← CRITICAL FOR JWT
//				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
//				.sessionManagement(session -> session
//						.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // For OAuth2
//				)
//				.build();
//	}


//	@Bean
//	public WebSecurityCustomizer webSecurityCustomizer() {
//		return web -> web.ignoring().requestMatchers(
//				"/favicon.ico",
//				"/error",
//				"/webjars/**",
//				"/static/**"
//		);
//	}

//	@Bean
//	public CookieSerializer cookieSerializer() {
//		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
//		serializer.setCookieName("OAUTH2SESSION");
//		serializer.setSameSite("Lax");  // Important for OAuth2
//		serializer.setUseHttpOnlyCookie(true);
//		serializer.setCookiePath("/");
//		return serializer;
//	}

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
