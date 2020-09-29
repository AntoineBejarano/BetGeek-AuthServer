package io.betgeek.authserver.service;

import java.util.List;

import io.betgeek.authserver.dto.PartnerUserDTO;
import io.betgeek.authserver.entity.PartnerUsers;

public interface PartnerUsersService {

	PartnerUsers findByUser(String idUser);
	List<PartnerUsers> findByPartner(String idPartner);
	List<PartnerUserDTO> getDTOByPartner(String idPartner);
	
}
