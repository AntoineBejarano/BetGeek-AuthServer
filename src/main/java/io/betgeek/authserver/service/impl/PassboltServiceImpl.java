package io.betgeek.authserver.service.impl;

import org.springframework.stereotype.Service;

import io.beetgeek.passbolt.client.PassboltClient;
import io.beetgeek.passbolt.exceptions.BadRequestException;
import io.beetgeek.passbolt.exceptions.PGPLibException;
import io.beetgeek.passbolt.exceptions.PassboltApiException;
import io.beetgeek.passbolt.exceptions.PassboltLibException;
import io.beetgeek.passbolt.model.User;
import io.beetgeek.passbolt.model.json.UserRequest;
import io.betgeek.authserver.entity.PassboltClientMainInfo;
import io.betgeek.authserver.exception.UserException;
import io.betgeek.authserver.service.PassboltService;

@Service
public class PassboltServiceImpl implements PassboltService {

	@Override
	public String saveUser(PassboltClientMainInfo mainInfo, UserRequest user) throws UserException {
		PassboltClient passbolt = initPassbolt(mainInfo);
		String userId = "";
		try {
			userId = passbolt.saveUser(user);
		} catch (PassboltLibException e) {
			throw new UserException("[Passbolt LIB Error] " + e.getMessage());
		} catch (PassboltApiException e) {
			throw new UserException("[Passbolt API Error] " + e.getMessage());
		} catch (BadRequestException e) {
			throw new UserException(e.getMessage());
		}
		return userId;
	}

	@Override
	public User getUser(PassboltClientMainInfo mainInfo, String userId) throws UserException {
		PassboltClient passbolt = initPassbolt(mainInfo);
		User user = null;
		try {
			user = passbolt.getUserById(userId);
		} catch (PassboltApiException e) {
			throw new UserException("[Passbolt LIB Error] " + e.getMessage());
		} catch (PassboltLibException e) {
			throw new UserException("[Passbolt API Error] " + e.getMessage());
		}
		
		return user;
	}
	
	private PassboltClient initPassbolt(PassboltClientMainInfo mainInfo) throws UserException  {
		PassboltClient client = new PassboltClient(mainInfo.getKeyId(), mainInfo.getPrivateKey(), mainInfo.getPassword());
		try {
			client.auth();
		} catch (PGPLibException e) {
			throw new UserException("[Passbolt PGP Error] " + e.getMessage());
		} catch (PassboltApiException e) {
			throw new UserException("[Passbolt API Error] " + e.getMessage());
		} catch (BadRequestException e) {
			throw new UserException("[Passbolt LIB Error] " + e.getMessage());
		}
		return client;
	}

}
