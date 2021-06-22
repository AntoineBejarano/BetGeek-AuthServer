package io.betgeek.authserver.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UserDTO {

	private String idUser;
	private String username;
	private Long idRole;
	private String idPassbolt;
	private String firstName;
	private String lastName;
	private Boolean status;
	private Boolean passboltComplete;
	private Long creditPoints;
	private Date createDate;
	private Date modifyDate;
	private String idCustomerStripe;
	private Boolean active;
	
	public String getFullName() {
		return (this.firstName != null ? (this.firstName + " ") : "") + (this.lastName != null ? (this.lastName) : "");
	}
	
}
