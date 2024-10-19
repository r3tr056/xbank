package com.xfinance.xbank.models.corebanking;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="users", uniqueConstraints={
	@UniqueConstraint(columnNames="username"),
	@UniqueConstraint(columnNames="email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="username")
	@NotBlank
	@NotNull
	@Size(max=20)
	private String username;

	@Column(name="email")
	@NotBlank
	@Email
	private String email;

	@Column(name="password")
	@NotNull
	@NotBlank
	@Length(min=8, max=50)
	private String password;

	@Column(name="locked")
	@NotBlank
	private Boolean locked;

	@Column(name="enabled")
	@NotBlank
	private Boolean enabled;

	@Column(name="credentials")
	@NotBlank
	private Boolean credentials;

	@Column(name="expired")
	@NotBlank
	private Boolean expired;

	@Column(name="expiry_date")
	@NotBlank
	private String expiry_date;


	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="user_roles", joinColumns=@JoinColumn(name="user_id"), inverseJoinColumn=@JoinColumn(name="role_id"))
	private Set<Role> roles = new HashSet<>();

	@Override
	public Collection<? extends GrantedAuthority

}