package io.betgeek.authserver.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.beetgeek.passbolt.exceptions.BadRequestException;
import io.betgeek.authserver.dto.PartnerUserDTO;
import io.betgeek.authserver.entity.PartnerUsers;
import io.betgeek.authserver.repository.PartnerUsersRepository;
import io.betgeek.authserver.service.PartnerUsersService;
import io.betgeek.authserver.service.UserService;
import io.betgeek.authserver.vo.UserVO;

@Service
public class PartnerUserServiceImpl implements PartnerUsersService {

	@Autowired
	private PartnerUsersRepository partnerUserRepository;
	
	@Autowired
	private UserService userService;
	
	@Override
	public PartnerUsers findByUser(String idUser) {
		return partnerUserRepository.findByIdUserAndActive(idUser, true);
	}

	@Override
	public List<PartnerUsers> findByPartner(String idPartner) {
		return partnerUserRepository.findByIdPartnerAndActive(idPartner, true);
	}

	@Override
	public List<PartnerUserDTO> getDTOByPartner(String idPartner) throws BadRequestException {
		List<PartnerUserDTO> userList = new ArrayList<>();
		List<PartnerUsers> partnerUsers = findByPartner(idPartner);
		for (PartnerUsers partnerUser : partnerUsers) {
			UserVO user = userService.getUser(partnerUser.getIdUser());
			if (user != null) {
				userList.add(new PartnerUserDTO(user.getUserId(), user.getFirstName(), user.getLastName()));
			}
		}
		return userList;
	}

	@Override
	public PartnerUserDTO getDTOByPartnerAndUser(String idPartner, String idUser) throws BadRequestException {
		PartnerUsers partnerUser = partnerUserRepository.findByIdPartnerAndIdUserAndActive(idPartner, idUser, true);
		UserVO user = userService.getUser(partnerUser.getIdUser());
		return new PartnerUserDTO(user.getUserId(), user.getFirstName(), user.getLastName());
	}

	@Override
	@Transactional
	public void delete(String idPartner, String idUser) {
		partnerUserRepository.deleteByIdPartnerAndIdUser(idPartner, idUser);
	}

}
