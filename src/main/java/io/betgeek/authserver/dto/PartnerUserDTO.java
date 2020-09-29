package io.betgeek.authserver.dto;

import lombok.Data;

@Data
public class PartnerUserDTO {

	private String idUser;
	private String firstName;
	private String lastName;
	
	public PartnerUserDTO() {
		
	}
	
	public PartnerUserDTO(String idUser, String firstName, String lastName) {
		this.idUser = idUser;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
}
