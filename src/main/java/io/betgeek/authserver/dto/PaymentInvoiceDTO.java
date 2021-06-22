package io.betgeek.authserver.dto;

import java.sql.Timestamp;

import io.betgeek.domain.persistence.entity.PaymentInvoicePersistenceEntity;
import io.betgeek.enums.PaymentInvoiceState;
import lombok.Data;

@Data
public class PaymentInvoiceDTO {

	private String idPaymentInvoice;
	private String paymentProvider;
	private String idInvoice;
	private String invoiceNumber;
	private String idUser;
	private Long idCurrency;
	private Double paymentAmount;
	private String serviceName;
	private String idServiceProvider;
	private PaymentInvoiceState invoiceState;
	private Timestamp createDate;
	private Timestamp sendingDate;
	private Timestamp paymentDate;
	private Timestamp finalizedDate;
	private Timestamp expirationDate;
	
	public PaymentInvoicePersistenceEntity toEntity() {
		PaymentInvoicePersistenceEntity entity = new PaymentInvoicePersistenceEntity();
		entity.setIdPaymentInvoice(this.idPaymentInvoice);
		entity.setPaymentProvider(this.paymentProvider);
		entity.setIdInvoice(this.idInvoice);
		entity.setIdUser(this.idUser);
		entity.setIdCurrency(this.idCurrency);
		entity.setPaymentAmount(this.paymentAmount);
		entity.setServiceName(this.serviceName);
		entity.setIdServiceProvider(this.idServiceProvider);
		entity.setInvoiceState(this.invoiceState.name());
		entity.setCreateDate(this.createDate);
		entity.setSendingDate(this.sendingDate);
		entity.setPaymentDate(this.paymentDate);
		entity.setFinalizedDate(this.finalizedDate);
		entity.setExpirationDate(this.expirationDate);
		return entity;
	}
	
}
