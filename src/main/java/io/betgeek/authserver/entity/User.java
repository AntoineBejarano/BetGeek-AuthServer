package io.betgeek.authserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "main_users")
@Data
public class User {

	@Id
	private String id;
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String password;

	@Column(name = "id_role")
	private Long idRole;

	@Column(name = "id_passbolt")
	private String idPassbolt;
	@Column(nullable = false)
	private Boolean active;
	
}
