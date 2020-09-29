package io.betgeek.authserver.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public List<PartnerUserDTO> getDTOByPartner(String idPartner) {
		List<PartnerUserDTO> userList = new ArrayList<PartnerUserDTO>();
		List<PartnerUsers> partnerUsers = findByPartner(idPartner);
		for (PartnerUsers partnerUser : partnerUsers) {
			UserVO user = userService.getUser(partnerUser.getIdUser());
			if (user != null) {
				userList.add(new PartnerUserDTO(user.getUserId(), user.getFirstName(), user.getLastName()));
			}
		}
		return userList;
	}

}
