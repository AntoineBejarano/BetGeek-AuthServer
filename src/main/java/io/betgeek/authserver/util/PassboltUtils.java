package io.betgeek.authserver.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
public class PassboltUtils {
	
	@Value("${betgeek.passbolt.admin.publicKey}")
	private String adminPublicKey;
	
	@Value("${betgeek.passbolt.admin.privateKey}")
	private String adminPrivateKey;
	
	private final String PUBLIC_KEY_PATH = "passbolt-keys/public/";
	private final String PRIVATE_KEY_PATH = "passbolt-keys/private/";
	private final String PUBLIC_KEY_INIT_NAME = "passbolt-keys/public/ppbl-";
	private final String PRIVATE_KEY_INIT_NAME = "passbolt-keys/private/pprv-";
	
	private final String FILE_EXTENSION = ".txt";

	public File getPublicKeyFileWithUserId(String userId) {
		return getKeyFileWithUserIdAndType(userId, PUBLIC_KEY_INIT_NAME);
	}
	
	public File getPrivateKeyFileWithUserId(String userId) {
		return getKeyFileWithUserIdAndType(userId, PRIVATE_KEY_INIT_NAME);
	}

	public InputStream getPublicKeyInputStreamFileWithUserId(String userId) throws IOException {
		return getInputStreamFileWithUserIdAndType(userId, PUBLIC_KEY_INIT_NAME);
	}
	
	public InputStream getPrivateKeyInputStreamFileWithUserId(String userId) throws IOException {
		return getInputStreamFileWithUserIdAndType(userId, PRIVATE_KEY_INIT_NAME);
	}
	
	private File getKeyFileWithUserIdAndType(String userId, String KeyType) {
		try {
			String path = getPublicKeyFileName(userId, KeyType);
			System.out.println(path);
			return ResourceUtils.getFile(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private InputStream getInputStreamFileWithUserIdAndType(String userId, String KeyType) throws IOException {
		return new ClassPathResource(getPublicKeyFileName(userId, KeyType)).getInputStream();
	}
	
	public InputStream getPublicKeyFileAdminInputStream() throws IOException {
		return new ClassPathResource(PUBLIC_KEY_PATH.concat(adminPublicKey)).getInputStream();
	}
	
	public InputStream getPrivateKeyFileAdminInputStream() throws IOException {
		return new ClassPathResource(PRIVATE_KEY_PATH.concat(adminPrivateKey)).getInputStream();
	}
	
	private String getPublicKeyFileName(String userId, String keyType) {
		return keyType.concat(userId).concat(FILE_EXTENSION);
	}
}
