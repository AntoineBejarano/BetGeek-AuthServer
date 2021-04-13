package io.betgeek.authserver.service;

import io.betgeek.authserver.dto.RegisterKeyDTO;

public interface RegisterKeyService {

	RegisterKeyDTO getByRegisterKeyId(String registerKeyId) throws Exception;
	void save(RegisterKeyDTO registerKey);
	
}
