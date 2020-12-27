package io.betgeek.authserver.mapper;

import org.springframework.stereotype.Component;

import io.betgeek.authserver.entity.User;
import io.betgeek.authserver.vo.UserVO;

@Component
public class UserMapper {

	public UserVO entityToVo(User user) {
		return new UserVO(user.getUsername(), user.getFirstName(), user.getLastName(), user.getIdRole(), user.getId(), user.getActive());
	}
	
}
