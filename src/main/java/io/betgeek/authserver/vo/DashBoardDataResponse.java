package io.betgeek.authserver.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashBoardDataResponse {

	private Double profitability;
	private Double totalBet;
	private Double totalWin;
	private Double totalBalance;

}
