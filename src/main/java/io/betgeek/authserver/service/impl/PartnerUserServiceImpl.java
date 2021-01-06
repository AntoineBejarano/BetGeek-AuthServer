package io.betgeek.authserver.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.beetgeek.passbolt.exceptions.BadRequestException;
import io.betgeek.authserver.dto.PartnerUserDTO;
import io.betgeek.authserver.entity.PartnerUsers;
import io.betgeek.authserver.enums.Roles;
import io.betgeek.authserver.exception.UserException;
import io.betgeek.authserver.repository.BetsPlacedRepository;
import io.betgeek.authserver.repository.PartnerUsersRepository;
import io.betgeek.authserver.service.PartnerUsersService;
import io.betgeek.authserver.service.UserService;
import io.betgeek.authserver.vo.DashBoardDataResponse;
import io.betgeek.authserver.vo.UserVO;

@Service
public class PartnerUserServiceImpl implements PartnerUsersService {

	@Autowired
	private PartnerUsersRepository partnerUserRepository;
	
	@Autowired
	private BetsPlacedRepository betsPlacedRepository;
	
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
	public List<PartnerUserDTO> getDTOByPartner(String idPartner, String rolId) throws BadRequestException {
		List<PartnerUserDTO> userList = new ArrayList<>();
		if(Long.valueOf(rolId) == Roles.ADMIN.id()) {
			List<PartnerUsers> allUsers = partnerUserRepository.findAll();		
			userList = getUserVOList(allUsers);
		} else if(Long.valueOf(rolId) == Roles.PARTNER.id()) {
			List<PartnerUsers> partnerUsers = findByPartner(idPartner);		
			userList = getUserVOList(partnerUsers);
		}
		return userList;
	}

	private List<PartnerUserDTO> getUserVOList(List<PartnerUsers> allUsers) throws BadRequestException {
		List<PartnerUserDTO> userList = new ArrayList<>();
		for (PartnerUsers partnerUser : allUsers) {
			UserVO user = userService.getUser(partnerUser.getIdUser());
			if (user != null) {
				userList.add(new PartnerUserDTO(user.getUserId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getActive()));
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

	@Override
	public void save(String partnerId, UserVO user) throws UserException, BadRequestException {
		String saveUserId = userService.saveUser(user);
		PartnerUsers partnerUser = new PartnerUsers();
		partnerUser.setIdPartner(partnerId);
		partnerUser.setActive(true);
		partnerUser.setIdUser(saveUserId);
		partnerUserRepository.save(partnerUser);
	}

	@Override
	public void updateUser(String partnerId, UserVO user) throws BadRequestException {
		userService.updateUser(user);
	}

	@Override
	public void changeState(String idUser) throws BadRequestException {
		UserVO user = userService.getUser(idUser);
		user.setActive(!user.getActive());
		userService.updateUser(user);
	}
	
	@Override
	public DashBoardDataResponse getDashBoardData(String userId, String rolId) throws BadRequestException {
		List<String> userList = new ArrayList<>();
		if(Long.valueOf(rolId) == Roles.ADMIN.id()) {
			return betsPlacedRepository.getUserDashBoardDataForGodUser();
		} else if(Long.valueOf(rolId) == Roles.PARTNER.id()) {
			List<PartnerUsers> partnerUsers = findByPartner(userId);
			userList.add(userId);
			for (PartnerUsers partnerUser : partnerUsers) {
				userList.add(partnerUser.getIdUser());
			}
		} else {
			userList.add(userId);
		}
		return betsPlacedRepository.getUserDashBoardData(userList);
	}

}
