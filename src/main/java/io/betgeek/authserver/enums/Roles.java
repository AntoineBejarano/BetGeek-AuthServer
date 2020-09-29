package io.betgeek.authserver.enums;

public enum Roles {
	
	ADMIN(1l),
	PARTNER(2l),
	USER(3l);
	
	private Long id;
	
	Roles(Long id) {
		this.id = id;
	}
	
	public Long id() {
		return this.id;
	}
}
