package io.betgeek.authserver.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "users", schema = "betgeek_auth")
@Data
public class User {

	@Id
	@Column(name = "id_user")
	private String id;
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String password;

	@Column(name = "id_role")
	private Long idRole;

	@Column(name = "id_passbolt")
	private String idPassbolt;
	
	@Column(nullable = false, name = "state")
	private Boolean active;
	
	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "passbolt_complete")
	private Boolean passboltComplete;
	
	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "update_date")
	private Date modifyDate;
	
	@PrePersist
	protected void onCreate() {
		createDate = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		modifyDate = new Date();
	}
	
}
