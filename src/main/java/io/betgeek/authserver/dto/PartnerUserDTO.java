package io.betgeek.authserver.dto;

import lombok.Data;

@Data
public class PartnerUserDTO {

	private String idUser;
	private String firstName;
	private String lastName;
	private String username;
	private Boolean active;
	private String email;
	
	public PartnerUserDTO() {
		
	}
	
	public PartnerUserDTO(String idUser, String firstName, String lastName) {
		this.idUser = idUser;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public PartnerUserDTO(String idUser, String firstName, String lastName, String username, String email, Boolean active) {
		this.idUser = idUser;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.active = active;
	}
	
}
