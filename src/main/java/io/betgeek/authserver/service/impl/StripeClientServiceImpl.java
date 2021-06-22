package io.betgeek.authserver.service.impl;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceItem;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.InvoiceCreateParams;
import com.stripe.param.InvoiceItemCreateParams;

import io.betgeek.authserver.dto.PaymentInvoiceDTO;
import io.betgeek.authserver.dto.UserDTO;
import io.betgeek.authserver.exception.ValidationException;
import io.betgeek.authserver.service.StripeClientService;
import io.betgeek.enums.Currency;
import io.betgeek.enums.PaymentInvoiceState;

@Service
public class StripeClientServiceImpl implements StripeClientService {

	private String stripeApiKey = "sk_live_...FHBi";
	private String defaultDescription = "BetGeek Client";
	private String defaultProductId = "prod_JFtK2KOBW4cft3";
	private String defaultPriceId = "price_1IdNXeKLX2NA91R8Hg8mz0NA";

	@Override
	public String registerCustomer(UserDTO user) throws ValidationException {
		Stripe.apiKey = stripeApiKey;

		CustomerCreateParams params = CustomerCreateParams.builder().setName(user.getFullName())
				.setEmail(user.getUsername()).setDescription(defaultDescription).build();

		try {
			Customer customer = Customer.create(params);
			return customer.getId();
		} catch (StripeException e) {
			throw new ValidationException(e.getMessage());
		}
	}

	@Override
	public PaymentInvoiceDTO createAndSendInvoice(UserDTO user) throws ValidationException {
		try {
			Stripe.apiKey = stripeApiKey;
			PaymentInvoiceDTO paymentInvoice = new PaymentInvoiceDTO();
			paymentInvoice.setCreateDate(new Timestamp(System.currentTimeMillis()));

			if (user.getIdCustomerStripe() == null || user.getIdCustomerStripe().isEmpty()) {
				throw new ValidationException("IdCustomStripe not found");
			}
			
			Product product = Product.retrieve(defaultProductId);
			Price price = Price.retrieve(defaultPriceId);

			InvoiceItemCreateParams paramsInvoiceItem = InvoiceItemCreateParams.builder().setPrice(price.getId())
					.setCustomer(user.getIdCustomerStripe()).build();

			InvoiceItem.create(paramsInvoiceItem);

			InvoiceCreateParams paramsInvoice = InvoiceCreateParams.builder().setCustomer(user.getIdCustomerStripe())
					.setCollectionMethod(InvoiceCreateParams.CollectionMethod.SEND_INVOICE).setDaysUntilDue(7l)
					.build();

			Invoice invoice = Invoice.create(paramsInvoice).sendInvoice();
			
			paymentInvoice.setPaymentProvider("STRIPE");
			paymentInvoice.setIdInvoice(invoice.getId());
			paymentInvoice.setInvoiceNumber(invoice.getNumber());
			paymentInvoice.setIdUser(user.getIdUser());
			paymentInvoice.setIdCurrency(Currency.EURO.getId());
			paymentInvoice.setPaymentAmount((price.getUnitAmount().doubleValue() / 100));
			paymentInvoice.setServiceName(product.getDescription());
			paymentInvoice.setIdServiceProvider(price.getId());
			paymentInvoice.setInvoiceState(getPaymentInvoiceStateByInvoiceStatus(invoice.getStatus()));
			
			if (paymentInvoice.getInvoiceState().equals(PaymentInvoiceState.SENT)) {
				paymentInvoice.setSendingDate(new Timestamp(System.currentTimeMillis()));
			}
			paymentInvoice.setExpirationDate(new Timestamp(invoice.getDueDate()));

			return paymentInvoice;
		} catch (StripeException e) {
			throw new ValidationException(e.getMessage());
		}
	}
	
	private PaymentInvoiceState getPaymentInvoiceStateByInvoiceStatus(String invoiceStatus) {
		switch (invoiceStatus) {
			case "created":
				return PaymentInvoiceState.CREATED;
			case "sent":
				return PaymentInvoiceState.SENT;
			case "paid":
				return PaymentInvoiceState.PAID;
			case "finalized":
				return PaymentInvoiceState.FINALIZED;
			default:
				return PaymentInvoiceState.ERROR;
		}
	}

}
