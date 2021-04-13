package io.betgeek.authserver.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.betgeek.domain.persistence.entity.RegisterKeyPersistenceEntity;
import io.betgeek.domain.persistence.repository.RegisterKeyPersistenceRepository;
import io.betgeek.authserver.dto.RegisterKeyDTO;
import io.betgeek.authserver.service.RegisterKeyService;
import io.betgeek.authserver.util.Mappers;

@Service
public class RegisterKeyServiceImpl implements RegisterKeyService {

	@Autowired
	private RegisterKeyPersistenceRepository registerKeyPersistenceRepository;
	
	@Autowired
	private Mappers mappers;
	
	@Override
	public RegisterKeyDTO getByRegisterKeyId(String registerKeyId) throws Exception {
		Optional<RegisterKeyPersistenceEntity> registerKeyOpt = registerKeyPersistenceRepository.findById(registerKeyId);
		if (!registerKeyOpt.isPresent()) {
			throw new Exception("La clave de registro [" + registerKeyId + "] no es valida!");
		}
		
		RegisterKeyPersistenceEntity registerKey = registerKeyOpt.get();
		if (registerKey.getIdUser() != null || !registerKey.getState()) {
			throw new Exception("La clave de registro [" + registerKeyId + "] ya fue usada!");
		}
		
		return mappers.registerKeyPersistenceEntityToDTO(registerKey);
	}

	@Override
	public void save(RegisterKeyDTO registerKey) {
		registerKeyPersistenceRepository.save(registerKey.toPersistenceEntity());
	}

}
