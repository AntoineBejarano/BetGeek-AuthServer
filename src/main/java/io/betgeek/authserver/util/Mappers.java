package io.betgeek.authserver.util;

import io.betgeek.domain.persistence.entity.RegisterKeyPersistenceEntity;

import org.springframework.stereotype.Component;

import io.betgeek.authserver.dto.RegisterKeyDTO;

@Component
public class Mappers {

	public RegisterKeyDTO registerKeyPersistenceEntityToDTO(RegisterKeyPersistenceEntity entity) {
		if (entity == null) return null;
		RegisterKeyDTO dto = new RegisterKeyDTO();
		dto.setRegisterKeyId(entity.getIdRegisteKey());
		dto.setPartnerId(entity.getIdPartner());
		dto.setUserId(entity.getIdUser());
		dto.setCreateDate(entity.getCreateDate());
		dto.setActivateDate(entity.getActivationDate());
		dto.setState(entity.getState());
		dto.setFreeTrial(entity.getFreeTrial());
		return dto;
	}
}
