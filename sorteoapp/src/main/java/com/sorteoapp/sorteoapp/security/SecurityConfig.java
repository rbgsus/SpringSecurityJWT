package com.sorteoapp.sorteoapp.security;

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

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;

	// Aquí debes inyectar tu filtro personalizado cuando lo tengas listo
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.exceptionHandling(ex -> ex.authenticationEntryPoint(customAuthenticationEntryPoint))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/tarjetas/**")
						.hasAnyRole("ADMIN", "USER").requestMatchers(HttpMethod.POST, "/user/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/user/all").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/tarjetas/todas**").permitAll()
						.requestMatchers(HttpMethod.POST, "/tarjeta/**").hasAnyRole("ADMIN", "USER")
						.requestMatchers(HttpMethod.POST, "/auth/login**").hasAnyRole("ADMIN", "USER", "GUEST")
						.requestMatchers(HttpMethod.PUT, "/tarjeta/**").hasRole("ADMIN")

						// CREAR METODO QUE SOLO PUEDA EDITAR LA PROPIA TARJETA DEL USUARIO
						// .requestMatchers(HttpMethod.PUT, "/tarjeta/usuario/*").hasRole("USER")

						.requestMatchers(HttpMethod.PUT, "/user/edit/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/user/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/tarjeta/**").hasRole("ADMIN")
						
						// CREAR METODO PARA QUE EL PROPIO USUARIO PUEDA BLOQUEAR Y SOLICITAR SU ELIMINACIÓN
						//  .requestMatchers(HttpMethod.DELETE, "/tarjeta/**").hasRole("USER")
						
						.anyRequest()
						.denyAll());

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
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
