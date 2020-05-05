package io.betgeek.authserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "roles_permissions")
@Data
public class RolePermission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "id_rol")
	private Long idRol;
	@Column(name = "id_permission")
	private Long idPermission;
	private Boolean active;
	
}
