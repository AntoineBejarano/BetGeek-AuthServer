package io.betgeek.authserver.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "partner_users", schema = "betgeek_auth")
@Data
public class PartnerUsers {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_partner_users;
	
	@Column(name = "id_partner")
	private String idPartner;
	
	@Column(name = "id_user")
	private String idUser;
	
	@Column(nullable = false, name = "state")
	private Boolean active;
	
	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "modify_date")
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
