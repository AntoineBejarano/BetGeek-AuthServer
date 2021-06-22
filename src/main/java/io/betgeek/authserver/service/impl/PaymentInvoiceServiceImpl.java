package io.betgeek.authserver.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.betgeek.authserver.dto.PaymentInvoiceDTO;
import io.betgeek.authserver.dto.UserDTO;
import io.betgeek.authserver.exception.ValidationException;
import io.betgeek.authserver.service.PaymentInvoceService;
import io.betgeek.authserver.service.StripeClientService;
import io.betgeek.domain.persistence.entity.UserPersistenceEntity;
import io.betgeek.domain.persistence.repository.PaymentInvoicePersistenceRepository;
import io.betgeek.domain.persistence.repository.UserPersistenceRepository;

@Service
public class PaymentInvoiceServiceImpl implements PaymentInvoceService {

	@Autowired
	private PaymentInvoicePersistenceRepository paymentInvoicePersistenceRepository;
	
	@Autowired
	private UserPersistenceRepository userPersistenceRepository;
	
	@Autowired
	private StripeClientService stripeClientService;
	
	@Override
	public void sendInvoiceToClient(UserDTO user) throws ValidationException {
		if (user.getIdCustomerStripe() == null || user.getIdCustomerStripe().isEmpty()) {
			String idCustomerStripe = stripeClientService.registerCustomer(user);
			user.setIdCustomerStripe(idCustomerStripe);
		}
		
		UserPersistenceEntity userP = userPersistenceRepository.findById(user.getIdUser()).orElse(null);
		if (userP != null) {
			userP.setIdCustomerStripe(user.getIdCustomerStripe());
			userPersistenceRepository.save(userP);			
		}
		
		PaymentInvoiceDTO paymentInvoice = stripeClientService.createAndSendInvoice(user);
		paymentInvoice.setIdPaymentInvoice(UUID.randomUUID().toString());
		paymentInvoicePersistenceRepository.save(paymentInvoice.toEntity());
	}

}
