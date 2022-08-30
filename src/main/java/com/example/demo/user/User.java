package com.example.demo.user;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table
public class User implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;
	@SequenceGenerator(
			name = "user_sequence",
			sequenceName = "user_sequence",
			allocationSize = 1)
	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "user_sequence")
	private Long id;
	private String name;
	private String surname;
	private String email;
	private String password;
	@Enumerated(EnumType.STRING)
	private UserRole userRole;
	private Boolean locked = false;
	private Boolean enabled = false;

	public User(String name, String surname, String email, String password, UserRole userRole) {
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.userRole = userRole;

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole.name());
		return Collections.singletonList(authority);
	}

	@Override
	public String getPassword() {

		return password;
	}

	@Override
	public String getUsername() {

		return email;
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return !locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {

		return enabled;
	}

}
