package io.betgeek.authserver.vo;

import lombok.Data;

@Data
public class UserVO {
	
	private String userId;
	private String personId;
	private String username;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String phoneCountryCode;
	private Integer idCountry;
	private CountryVO country;
	private Integer idCurrency;
	private CurrencyVO currency;
	private String address;
	private String identificationNumber;
	private String password;
	private Long rolId;
	private Boolean active;
	private String registerKeyId;
	
	public UserVO() {
		
	}
	
	public UserVO(String username, String firstName, String lastName, Long rolId, String userId, Boolean active) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.rolId = rolId;
		this.userId = userId;
		this.active = active;
	}
	
}
