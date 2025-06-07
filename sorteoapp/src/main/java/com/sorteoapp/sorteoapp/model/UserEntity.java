package com.sorteoapp.sorteoapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
// TODO eliminar @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) cuando haga el dto de tarjeta
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UserEntity implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String dni;

	@NotBlank(message = "El campo del nombre no puede estar vacío")
	@NotEmpty(message = "El campo del nombre no puede estar vacío")
	@Column(nullable = false)
	private String name;

	@NotBlank(message = "El campo del nombre de usuario nick no puede estar vacío")
	@NotEmpty(message = "El campo del nombre de usuario nick no puede estar vacío")
	@Column(unique = true, nullable = false)
	private String username;

	@NotBlank(message = "El campo del primer apellido no puede estar vacío")
	@NotEmpty(message = "El campo del primer apellido no puede estar vacío")
	@Column(nullable = false)
	private String firstName;

	private String lastName;

	@NotBlank(message = "El campo del email no puede estar vacío")
	@NotEmpty(message = "El campo del email no puede estar vacío")
	@Column(unique = true, nullable = false)
	@Email(message = "El email no está correctamente formado")
	private String email;

	private String phone;

	@NotBlank(message = "El campo de la contraseña no puede estar vacío")
	@NotEmpty(message = "El campo de la contraseña no puede estar vacío")
	@Column(nullable = false)
	private String password;

	@Builder.Default
	@JsonFormat(pattern = "yyyy-MM-dd") // Formato para LocalDate
	private LocalDate fechaNacimiento = LocalDate.now();

	private String avatar;

	@Builder.Default
	@JsonIgnore // Evita la serialización de tarjetas dentro del usuario
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Tarjeta> tarjetas = new ArrayList<>();

	@Builder.Default
	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private Set<UserRole> roles = new HashSet<>();

	@CreatedDate
	private LocalDateTime createdAt;

	@Builder.Default
	private LocalDateTime lastPasswrodChangedAt = LocalDateTime.now();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (roles == null) {
			return Collections.emptyList();
		}
		return roles.stream().map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.name())) // ✅ con guion bajo
				.collect(Collectors.toList());
	}

	// Sobrescribe el método de la interfaz UserDetails
	@Override
	public boolean isAccountNonExpired() {
		// Retorna true si la cuenta NO está expirada.
		// Si retornara false, Spring Security impediría al usuario iniciar sesión.
		// Aquí, al devolver true siempre, estamos indicando que nunca caduca la cuenta.
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// Retorna true si la cuenta NO está bloqueada.
		// Si se devuelve false, Spring Security bloquearía el acceso al usuario.
		// En una app real, podrías verificar aquí un campo tipo "bloqueado" desde la
		// base de datos.
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// Retorna true si las credenciales (como la contraseña) NO han expirado.
		// Si devolvemos false, el usuario tendría que cambiar su contraseña para
		// autenticarse.
		// Ideal para forzar cambios de contraseña periódicamente, aunque aquí se
		// ignora.
		return true;
	}

	@Override
	public boolean isEnabled() {
		// Retorna true si la cuenta está habilitada.
		// Si se devuelve false, el usuario no podrá autenticarse.
		// Esto suele usarse para activar o desactivar cuentas sin borrarlas.
		return true;
	}

}
