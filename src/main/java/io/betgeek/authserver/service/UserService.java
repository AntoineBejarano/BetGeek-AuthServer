package io.betgeek.authserver.service;

import javax.ws.rs.BadRequestException;

import org.springframework.web.multipart.MultipartFile;

import io.betgeek.authserver.entity.PassboltClientMainInfo;
import io.betgeek.authserver.exception.RedirecException;
import io.betgeek.authserver.exception.UserException;
import io.betgeek.authserver.vo.UserVO;

public interface UserService {

	String saveUser(PassboltClientMainInfo mainInfo, UserVO user) throws UserException, BadRequestException;
	void savePassboltData(PassboltClientMainInfo mainInfo, String userId, MultipartFile privateFile) throws UserException, BadRequestException, RedirecException;
	UserVO getUser(String userId) throws BadRequestException;
	void updateUser(UserVO user) throws BadRequestException;
	
}
