package io.betgeek.authserver.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.beetgeek.passbolt.model.json.UserRequest;
import io.betgeek.authserver.entity.PassboltClientMainInfo;
import io.betgeek.authserver.exception.RedirecException;
import io.betgeek.authserver.exception.UserException;
import io.betgeek.authserver.mapper.UserMapper;
import io.betgeek.authserver.service.PassboltService;
import io.betgeek.authserver.service.UserService;
import io.betgeek.authserver.vo.UserVO;
import io.betgeek.domain.persistence.entity.CountryPerisistenceEntity;
import io.betgeek.domain.persistence.entity.CurrencyPersistenceEntity;
import io.betgeek.domain.persistence.entity.PassboltUserPersistenceEntity;
import io.betgeek.domain.persistence.entity.PersonPersistenceEntity;
import io.betgeek.domain.persistence.entity.UserPersistenceEntity;
import io.betgeek.domain.persistence.repository.PassboltUserPersistenceRepository;
import io.betgeek.domain.persistence.repository.PersonPersistenceRepository;
import io.betgeek.domain.persistence.repository.UserPersistenceRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private PassboltService passboltService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserPersistenceRepository userRepository;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private PassboltUserPersistenceRepository passboltUserRepository;
	
	@Autowired
	private PersonPersistenceRepository personPersistenceRepository;
	
//	@Autowired
//	private RegisterKeyService registerKeyService;
	
//	@Autowired
//	private PartnerUsersPersistenceRepository partnerUsersRepository;
//	
//	@Autowired
//	private PaymentInvoicePersistenceRepository paymentInvoicePersistenceRepository;
//	
//	@Autowired
//	private ServiceSubscriptionPersistenceRepository serviceSubscriptionPersistenceRepository;
	
	private static final String PRIVATE_KEY_BEGIN = "-----BEGIN PGP PRIVATE KEY BLOCK-----";
//	private static final String PRIVATE_KEY_VERSION = "Version: OpenPGP.js v4.6.2";
	private static final String PRIVATE_KEY_COMMENT = "Comment: https://openpgpjs.org";
	private static final String PRIVATE_KEY_END = "-----END PGP PRIVATE KEY BLOCK-----";

	@Override
	public String saveUser(PassboltClientMainInfo mainInfo, UserVO userVo) throws UserException, BadRequestException {
		checkInfoUser(userVo);
		UserPersistenceEntity user = voToEntity(userVo);
		UserRequest userPassbolt = userAuthToUserPassbolt(user, userVo);
		String userId = UUID.randomUUID().toString();
		String passboltUserId = passboltService.saveUser(mainInfo, userPassbolt);
		
		user.setIdUser(userId);
		user.setIdRole(3l);
		user.setIdPassbolt(passboltUserId);
		user.setActive(false);
		user.setStatus(true);
		user.setPassboltComplete(false);
		
		PersonPersistenceEntity person = new PersonPersistenceEntity();
		person.setIdPerson(UUID.randomUUID().toString());
		person.setFirstName(userVo.getFirstName());
		person.setLastName(userVo.getLastName());
		person.setPhoneCountryCode(userVo.getPhoneCountryCode());
		person.setPhoneNumber(userVo.getPhoneNumber());
		person.setAddress(userVo.getAddress());
		person.setIdentificationNumber(userVo.getIdentificationNumber());
		
		if (userVo.getIdCountry() != null) {
			CountryPerisistenceEntity country = new CountryPerisistenceEntity();
			country.setIdCountry(userVo.getIdCountry().longValue());
			person.setCountry(country);
		}
		
		if (userVo.getIdCurrency() != null) {
			CurrencyPersistenceEntity currency = new CurrencyPersistenceEntity();
			currency.setIdCurrency(userVo.getIdCurrency().longValue());
			person.setCurrency(currency);
		}
		
		personPersistenceRepository.save(person);
		userRepository.save(user);
		
//		try {
//			RegisterKeyDTO registerKey = registerKeyService.getByRegisterKeyId(userVo.getRegisterKeyId());
//			if (!registerKey.getState()) {
//				throw new UserException("La clave de registro no es valida");
//			}
//			PartnerUsersPersistenceEntity partnerUser = new PartnerUsersPersistenceEntity();
//			partnerUser.setIdPartner(registerKey.getPartnerId());
//			partnerUser.setIdUser(userId);
//			partnerUser.setActive(true);
//			partnerUsersRepository.save(partnerUser);
//			
//			Timestamp dateNow = new Timestamp(System.currentTimeMillis());
//			registerKey.setUserId(userId);
//			registerKey.setState(false);
//			registerKey.setActivateDate(new Date(System.currentTimeMillis()));
//			registerKeyService.save(registerKey);
//
//			user.setActive(true);
//			userRepository.save(user);
//			
//			if (registerKey.getFreeTrial()) {
//				PaymentInvoicePersistenceEntity paymentInvoice = new PaymentInvoicePersistenceEntity();
//				String idPaymentInvoice = UUID.randomUUID().toString();
//				paymentInvoice.setIdPaymentInvoice(idPaymentInvoice);
//				paymentInvoice.setPaymentProvider("BETGEEK");
//				paymentInvoice.setIdInvoice(idPaymentInvoice);
//				paymentInvoice.setInvoiceNumber("FREE-TRIAL");
//				paymentInvoice.setIdUser(userId);
//				paymentInvoice.setIdCurrency(Currency.EURO.getId());
//				paymentInvoice.setPaymentAmount(0d);
//				paymentInvoice.setServiceName("Free Trial");
//				paymentInvoice.setInvoiceState(PaymentInvoiceState.PAID.toString());
//				paymentInvoice.setCreateDate(dateNow);
//				paymentInvoice.setPaymentDate(dateNow);
//				paymentInvoice.setFinalizedDate(dateNow);
//				paymentInvoice.setExpirationDate(dateNow);
//				paymentInvoicePersistenceRepository.save(paymentInvoice);
//				
//				Timestamp subscriptionInitialDate = new Timestamp(System.currentTimeMillis());
//				Long fourHours = 14400000l;
//				Timestamp subscriptionEndDate = new Timestamp(subscriptionInitialDate.getTime() + fourHours);
//				
//				ServiceSubscriptionPersistenceEntity subscription = new ServiceSubscriptionPersistenceEntity();
//				subscription.setIdServiceSubscription(UUID.randomUUID().toString());
//				subscription.setPaymentInvoice(paymentInvoice);
//				subscription.setInitDate(subscriptionInitialDate);
//				subscription.setEndDate(subscriptionEndDate);
//				subscription.setExpired(false);
//				subscription.setFreeTrial(true);
//				serviceSubscriptionPersistenceRepository.save(subscription);
//			}
//		} catch (Exception e) { }
		
		return userId;
	}

	@Override
	public void savePassboltData(PassboltClientMainInfo mainInfo, String userId, MultipartFile privateFile)
			throws UserException, BadRequestException, RedirecException {
		checkPrivateFile(privateFile);
		UserPersistenceEntity user = userRepository.findById(userId).get();
		if (user == null) {
			throw new BadRequestException("El usuario no existe!");
		}
		
		if (user.getPassboltComplete()) {
			throw new RedirecException("El usuario ya completo su registro");
		}
		
		io.beetgeek.passbolt.model.User userPassbolt = passboltService.getUser(mainInfo, user.getIdPassbolt());
		if (userPassbolt.getGpgkey() == null || userPassbolt.getRole() == null) {
			throw new BadRequestException("Tienes que completar el registro en la plataforma de Passbolt (https://passbolt.betgeek.io) para poder continuar");
		}

		PassboltUserPersistenceEntity newUserPassbolt = new PassboltUserPersistenceEntity();
		newUserPassbolt.setIdPassboltUser(user.getIdPassbolt());
		newUserPassbolt.setPassboltRoleId(userPassbolt.getRole_id());
		newUserPassbolt.setPassboltRole(userPassbolt.getRole().getId());
		newUserPassbolt.setKeyId(userPassbolt.getGpgkey().getKey_id());
		newUserPassbolt.setFingerprint(userPassbolt.getGpgkey().getFingerprint());
		newUserPassbolt.setType(userPassbolt.getGpgkey().getType());
		newUserPassbolt.setUid(userPassbolt.getGpgkey().getUid());
		try {
			newUserPassbolt.setPrivateKey(privateFile.getBytes());
			newUserPassbolt.setPublicKey(userPassbolt.getGpgkey().getArmored_key().getBytes());
		} catch (IOException e) {
			throw new BadRequestException("El archivo privado es obligatorio!");
		}
		
		passboltUserRepository.save(newUserPassbolt);
		user.setPassboltComplete(true);
		userRepository.save(user);
	}

	@Override
	public UserVO getUser(String userId) throws BadRequestException {
		Optional<UserPersistenceEntity> userOpt = userRepository.findById(userId);
		if (!userOpt.isPresent()) throw new BadRequestException("userId no encontrado");
		return userMapper.entityToVo(userOpt.get());
	}

	@Override
	public void updateUser(UserVO user) throws BadRequestException {
		checkInfoUpdateUser(user);
		UserPersistenceEntity entityUser = userRepository.findById(user.getUserId()).get();
		
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			entityUser.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		
		PersonPersistenceEntity person = personPersistenceRepository.findById(user.getPersonId()).orElse(null);
		if (person != null) {
			person.setFirstName(user.getFirstName());
			person.setLastName(user.getLastName());
			person.setPhoneNumber(user.getPhoneNumber());
			person.setPhoneCountryCode(user.getPhoneCountryCode());
			person.setIdentificationNumber(user.getIdentificationNumber());
			person.setAddress(user.getAddress());
			if (user.getIdCountry() != null) {
				CountryPerisistenceEntity country = new CountryPerisistenceEntity();
				country.setIdCountry(user.getIdCountry().longValue());
				person.setCountry(country);
			}
			
			if (user.getIdCurrency() != null) {
				CurrencyPersistenceEntity currency = new CurrencyPersistenceEntity();
				currency.setIdCurrency(user.getIdCurrency().longValue());
				person.setCurrency(currency);
			}
			
			personPersistenceRepository.save(person);
		}
		
		userRepository.save(entityUser);
	}

	private void checkInfoUser(UserVO user) throws BadRequestException {
		if (user.getUsername() == null || user.getUsername().isEmpty()
			|| user.getPassword() == null || user.getPassword().isEmpty()
			|| user.getFirstName() == null || user.getFirstName().isEmpty()
			|| user.getLastName() == null || user.getLastName().isEmpty()) {
			throw new BadRequestException("No se enviaron todos los campos");
		}
		UserPersistenceEntity validUser = userRepository.findByUsername(user.getUsername());
		if (validUser != null) {
			throw new BadRequestException("El email [" + user.getUsername() + "] ya esta registrado");
		}
//		try {
//			registerKeyService.getByRegisterKeyId(user.getRegisterKeyId());
//		} catch (Exception e) {
//			throw new BadRequestException(e.getMessage());
//		}
	}
	
	private void checkInfoUpdateUser(UserVO user) throws BadRequestException {
		if (user.getFirstName() == null || user.getFirstName().isEmpty()
			|| user.getLastName() == null || user.getLastName().isEmpty()) {
			throw new BadRequestException("No se enviaron todos los campos");
		}
	}
	
	private UserPersistenceEntity voToEntity(UserVO vo) {
		UserPersistenceEntity user = new UserPersistenceEntity();
		user.setUsername(vo.getUsername());
//		user.setFirstName(vo.getFirstName());
//		user.setLastName(vo.getLastName());
		user.setPassword(passwordEncoder.encode(vo.getPassword()));
		return user;
	}
	
	private UserRequest userAuthToUserPassbolt(UserPersistenceEntity user, UserVO userVo) {
		UserRequest userPassbolt = new UserRequest();
		userPassbolt.setUsername(user.getUsername());
		userPassbolt.setFirstName(userVo.getFirstName());
		userPassbolt.setLastName(userVo.getLastName());
		return userPassbolt;
	}
	
	private String inputStreamToString(InputStream inputStream) {
		String text = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
		    .lines()
		    .collect(Collectors.joining("\n"));
		return text;
	}
	
	private void checkPrivateFile(MultipartFile privateFile) throws BadRequestException {
		try {
			String privateText = inputStreamToString(privateFile.getInputStream());
			String[] privateTextSplit = privateText.split("\n");
			int lastLineIndex = privateTextSplit.length - 1;
			
			if (!privateTextSplit[0].toString().equals(PRIVATE_KEY_BEGIN) ||
				// !privateTextSplit[1].toString().equals(PRIVATE_KEY_VERSION) ||
				!privateTextSplit[2].toString().equals(PRIVATE_KEY_COMMENT) ||
				!privateTextSplit[lastLineIndex].toString().equals(PRIVATE_KEY_END)) {
				throw new BadRequestException("El archivo privado no es valido");
			}
		} catch (IOException e) {
			throw new BadRequestException("El archivo privado no es valido");
		} catch (Exception e) {
			throw new BadRequestException("El archivo privado no es valido");
		}
	}
}
