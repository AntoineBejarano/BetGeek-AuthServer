package io.betgeek.authserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.betgeek.authserver.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	User findByUsername(String usuario);
	
}
