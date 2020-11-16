package io.betgeek.authserver.service;

import java.util.List;

import io.beetgeek.passbolt.exceptions.BadRequestException;
import io.betgeek.authserver.dto.PartnerUserDTO;
import io.betgeek.authserver.entity.PartnerUsers;

public interface PartnerUsersService {

	PartnerUsers findByUser(String idUser);
	List<PartnerUsers> findByPartner(String idPartner);
	List<PartnerUserDTO> getDTOByPartner(String idPartner) throws BadRequestException;
	PartnerUserDTO getDTOByPartnerAndUser(String idPartner, String idUser) throws BadRequestException;
	void delete(String idPartner, String idUser);
	
}
