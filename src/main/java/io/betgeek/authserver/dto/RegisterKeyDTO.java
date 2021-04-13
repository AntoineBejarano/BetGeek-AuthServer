package io.betgeek.authserver.dto;

import java.util.Date;

import io.betgeek.domain.persistence.entity.RegisterKeyPersistenceEntity;
import lombok.Data;

@Data
public class RegisterKeyDTO {

	private String registerKeyId;
	private String partnerId;
	private String userId;
	private Date createDate;
	private Date activateDate;
	private Boolean state;
	
	public RegisterKeyPersistenceEntity toPersistenceEntity() {
		RegisterKeyPersistenceEntity entity = new RegisterKeyPersistenceEntity();
		entity.setIdRegisteKey(this.registerKeyId);
		entity.setIdPartner(this.partnerId);
		entity.setIdUser(this.userId);
		entity.setCreateDate(this.createDate);
		entity.setActivationDate(this.activateDate);
		entity.setState(this.state);
		return entity;
	}
	
}
