package com.sorteoapp.sorteoapp.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource())) // <-- habilitar CORS
				.csrf(csrf -> csrf.disable())
				.exceptionHandling(ex -> ex.authenticationEntryPoint(customAuthenticationEntryPoint))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/cards/**")
						.hasAnyRole("ADMIN", "USER").requestMatchers(HttpMethod.POST, "/user/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/user/all").permitAll()
						.requestMatchers(HttpMethod.GET, "/cards/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/user/me**").hasAnyRole("ADMIN", "USER")
						.requestMatchers(HttpMethod.POST, "/auth/login**").permitAll()
						.requestMatchers(HttpMethod.PUT, "/card/**").hasRole("ADMIN")
						// CREAR METODO QUE SOLO PUEDA EDITAR LA PROPIA TARJETA DEL USUARIO
						// .requestMatchers(HttpMethod.PUT, "/tarjeta/usuario/*").hasRole("USER")

						.requestMatchers(HttpMethod.GET, "/categorias/**").hasAnyRole("ADMIN", "USER")

						.requestMatchers(HttpMethod.PUT, "/user/edit/**").hasRole("USER")
						.requestMatchers(HttpMethod.DELETE, "/user/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/tarjeta/**").hasRole("ADMIN")

						// CREAR METODO PARA QUE EL PROPIO USUARIO PUEDA BLOQUEAR Y SOLICITAR SU
						// ELIMINACIÓN
						// .requestMatchers(HttpMethod.DELETE, "/tarjeta/**").hasRole("USER")
						.anyRequest().denyAll());

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// Configuración CORS
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:4200")); // URL de Angular
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
}
