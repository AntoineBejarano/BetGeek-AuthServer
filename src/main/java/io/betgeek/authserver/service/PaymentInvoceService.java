package io.betgeek.authserver.service;

import io.betgeek.authserver.dto.UserDTO;
import io.betgeek.authserver.exception.ValidationException;

public interface PaymentInvoceService {

	void sendInvoiceToClient(UserDTO user) throws ValidationException;
	
}
