package io.betgeek.authserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.betgeek.authserver.entity.PassboltUser;

public interface PassboltUserRespository extends JpaRepository<PassboltUser, Long> {

	PassboltUser findById(String id);
	
}
