package io.betgeek.authserver.service;

import java.util.List;

import io.betgeek.authserver.dto.PartnerUserDTO;
import io.betgeek.domain.persistence.entity.PartnerUsersPersistenceEntity;

public interface PartnerUsersService {

	PartnerUsersPersistenceEntity findByUser(String idUser);
	List<PartnerUsersPersistenceEntity> findByPartner(String idPartner);
	List<PartnerUserDTO> getDTOByPartner(String idPartner);
	
}
