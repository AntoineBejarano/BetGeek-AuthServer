package io.betgeek.authserver.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.betgeek.authserver.entity.BetsPlaced;
import io.betgeek.authserver.vo.DashBoardDataResponse;

@Repository
public interface BetsPlacedRepository extends JpaRepository<BetsPlaced, String> {

	@Query(value = "SELECT new io.betgeek.authserver.vo.DashBoardDataResponse(SUM(rentabilidadU) as profitability, SUM(totalAmountToBet) as totalAmountToBet, SUM(totalAmountToWin) as totalAmountToWin, SUM(totalBalance) as totalBalance) FROM BetsPlaced WHERE userId IN :users")
	public DashBoardDataResponse getUserDashBoardData(@Param("users") Collection<String> users);
	@Query(value = "SELECT new io.betgeek.authserver.vo.DashBoardDataResponse(SUM(rentabilidadU) as profitability, SUM(totalAmountToBet) as totalAmountToBet, SUM(totalAmountToWin) as totalAmountToWin, SUM(totalBalance) as totalBalance) FROM BetsPlaced")
	public DashBoardDataResponse getUserDashBoardDataForGodUser();
	
}
