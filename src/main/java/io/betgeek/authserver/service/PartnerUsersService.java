package io.betgeek.authserver.service;

import java.util.List;

import io.beetgeek.passbolt.exceptions.BadRequestException;
import io.betgeek.authserver.dto.PartnerUserDTO;
import io.betgeek.authserver.entity.PartnerUsers;
import io.betgeek.authserver.exception.UserException;
import io.betgeek.authserver.vo.DashBoardDataResponse;
import io.betgeek.authserver.vo.UserVO;

public interface PartnerUsersService {

	PartnerUsers findByUser(String idUser);
	List<PartnerUsers> findByPartner(String idPartner);
	List<PartnerUserDTO> getDTOByPartner(String idPartner, String rolId) throws BadRequestException;
	PartnerUserDTO getDTOByPartnerAndUser(String idPartner, String idUser) throws BadRequestException;
	void delete(String idPartner, String idUser);
	void save(String partnerId, UserVO user) throws UserException, BadRequestException;
	void updateUser(String partnerId, UserVO user) throws BadRequestException;
	void changeState(String idUser) throws BadRequestException;
	DashBoardDataResponse getDashBoardData(String userId, String rolId) throws BadRequestException;
	
}
