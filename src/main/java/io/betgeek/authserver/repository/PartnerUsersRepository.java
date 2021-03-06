package io.betgeek.authserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.betgeek.authserver.dto.PartnerUserDTO;
import io.betgeek.authserver.entity.PartnerUsers;

public interface PartnerUsersRepository extends JpaRepository<PartnerUsers, Long> {

	PartnerUsers findByIdUserAndActive(String idUser, Boolean active);
	List<PartnerUsers> findByIdPartnerAndActive(String idPartner, Boolean active);
	
}
