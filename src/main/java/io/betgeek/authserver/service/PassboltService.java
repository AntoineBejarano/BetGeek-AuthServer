package io.betgeek.authserver.service;

import io.beetgeek.passbolt.model.User;
import io.beetgeek.passbolt.model.json.UserRequest;
import io.betgeek.authserver.entity.PassboltClientMainInfo;
import io.betgeek.authserver.exception.UserException;

public interface PassboltService {

	String saveUser(PassboltClientMainInfo mainInfo, UserRequest user) throws UserException;
	User getUser(PassboltClientMainInfo mainInfo, String userId) throws UserException;
	
}
