package io.betgeek.authserver.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "bets_placed")
@Data
public class BetsPlaced {

	@Id
	private String betAccountId;
	
	@Column(name = "bettingProcessId")
	private String bettingProcessId;
	
	@Column(name = "bettingProcessInit")
	private String bettingProcessInit;
	
	@Column(name = "bettingProcessEnd")
	private String bettingProcessEnd;
	
	@Column(name = "betProvider")
	private String betProvider;
	
	@Column(name = "primaryHouse")
	private String primaryHouse;
	
	@Column(name = "secondaryHouse")
	private String secondaryHouse;
	
	@Column(name = "deport")
	private String deport;
	
	@Column(name = "league")
	private String league;
	
	@Column(name = "team1")
	private String team1;
	
	@Column(name = "team2")
	private String team2;
	
	@Column(name = "period")
	private String period;
	
	@Column(name = "primaryTitle")
	private String primaryTitle;
	
	@Column(name = "secondaryTitle")
	private String secondaryTitle;
	
	@Column(name = "primaryBetProviderId")
	private int primaryBetProviderId;
	
	@Column(name = "secondaryBetProviderId")
	private int secondaryBetProviderId;
	
	@Column(name = "primaryOdd")
	private Double primaryOdd;
	
	@Column(name = "secondaryOdd")
	private Double secondaryOdd;
	
	@Column(name = "primaryBalance")
	private Double primaryBalance;
	
	@Column(name = "secondaryBalance")
	private Double secondaryBalance;
	
	@Column(name = "primaryAmountToBet")
	private Double primaryAmountToBet;
	
	@Column(name = "secondaryAmountToBet")
	private Double secondaryAmountToBet;
	
	@Column(name = "primaryBeneficio")
	private int primaryBeneficio;
	
	@Column(name = "secondaryBeneficio")
	private int secondaryBeneficio;
	
	@Column(name = "primaryAmountToWin")
	private int primaryAmountToWin;
	
	@Column(name = "secondaryAmountToWin")
	private int secondaryAmountToWin;
	
	@Column(name = "primaryFirstPlaceBetTimestamp")
	private String primaryFirstPlaceBetTimestamp;
	
	@Column(name = "secondaryFirstPlaceBetTimestamp")
	private String secondaryFirstPlaceBetTimestamp;
	
	@Column(name = "primaryFirstPlaceBetStatus")
	private String primaryFirstPlaceBetStatus;
	
	@Column(name = "secondaryFirstPlaceBetStatus")
	private String secondaryFirstPlaceBetStatus;
	
	@Column(name = "primaryFinalPlaceBetStatus")
	private String primaryFinalPlaceBetStatus;
	
	@Column(name = "secondaryFinalPlaceBetStatus")
	private String secondaryFinalPlaceBetStatus;
	
	@Column(name = "primaryFinalOdd")
	private Double primaryFinalOdd;
	
	@Column(name = "secondaryFinalOdd")
	private Double secondaryFinalOdd;
	
	@Column(name = "primaryBetId")
	private int primaryBetId;
	
	@Column(name = "secondaryBetId")
	private int secondaryBetId;
	
	@Column(name = "cashOutHouse")
	private String cashOutHouse;
	
	@Column(name = "cashOutSuccess")
	private String cashOutSuccess;
	
	@Column(name = "isSureBet")
	private String isSureBet;
	
	@Column(name = "totalAmountToBet")
	private Double totalAmountToBet;
	
	@Column(name = "totalAmountToWin")
	private Double totalAmountToWin;
	
	@Column(name = "rentabilidadU")
	private Double rentabilidadU;
	
	@Column(name = "totalBalance")
	private Double totalBalance;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "modify_date")
	private Date modifyDate;
	
	@PrePersist
	protected void onCreate() {
		createDate = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		modifyDate = new Date();
	}
	
}
