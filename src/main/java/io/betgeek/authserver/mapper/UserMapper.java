package io.betgeek.authserver.mapper;

import org.springframework.stereotype.Component;

import io.betgeek.authserver.vo.CountryVO;
import io.betgeek.authserver.vo.CurrencyVO;
import io.betgeek.authserver.vo.UserVO;
import io.betgeek.domain.persistence.entity.UserPersistenceEntity;

@Component
public class UserMapper {

	public UserVO entityToVo(UserPersistenceEntity user) {
		UserVO userVo = new UserVO();
		userVo.setUsername(user.getUsername());
		userVo.setRolId(user.getIdRole());
		userVo.setUserId(user.getIdUser());
		userVo.setActive(user.getActive());
		if (user.getPerson() != null) {
			userVo.setPersonId(user.getPerson().getIdPerson());
			userVo.setFirstName(user.getPerson().getFirstName());
			userVo.setLastName(user.getPerson().getLastName());
			userVo.setPhoneCountryCode(user.getPerson().getPhoneCountryCode());
			userVo.setPhoneNumber(user.getPerson().getPhoneNumber());
			userVo.setIdentificationNumber(user.getPerson().getIdentificationNumber());
			userVo.setAddress(user.getPerson().getAddress());
			
			if (user.getPerson().getCountry() != null) {
				userVo.setIdCountry(user.getPerson().getCountry().getIdCountry().intValue());
				CountryVO country = new CountryVO();
				country.setIdCountry(user.getPerson().getCountry().getIdCountry().intValue());
				country.setCountry(user.getPerson().getCountry().getName());
				country.setCode(user.getPerson().getCountry().getCode());
			}
			
			if (user.getPerson().getCurrency() != null) {
				userVo.setIdCurrency(user.getPerson().getCurrency().getIdCurrency().intValue());
				CurrencyVO currency = new CurrencyVO();
				currency.setIdCurrency(user.getPerson().getCurrency().getIdCurrency().intValue());
				currency.setName(user.getPerson().getCurrency().getName());
				currency.setCode(user.getPerson().getCurrency().getCode());
				currency.setSymbol(user.getPerson().getCurrency().getSymbol());
			}
		}
		return userVo;
	}
	
}
