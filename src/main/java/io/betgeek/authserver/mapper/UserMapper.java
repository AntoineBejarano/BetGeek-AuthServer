package io.betgeek.authserver.mapper;

import org.springframework.stereotype.Component;

import io.betgeek.authserver.vo.UserVO;
import io.betgeek.domain.persistence.entity.UserPersistenceEntity;

@Component
public class UserMapper {

	public UserVO entityToVo(UserPersistenceEntity user) {
		return new UserVO(user.getUsername(), user.getFirstName(), user.getLastName(), user.getIdRole(), user.getIdUser(), user.getActive());
	}
	
}
