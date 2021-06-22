package io.betgeek.authserver.service;

import io.betgeek.authserver.dto.PaymentInvoiceDTO;
import io.betgeek.authserver.dto.UserDTO;
import io.betgeek.authserver.exception.ValidationException;

public interface StripeClientService {

	String registerCustomer(UserDTO user) throws ValidationException;
	PaymentInvoiceDTO createAndSendInvoice(UserDTO user) throws ValidationException;
	
}
