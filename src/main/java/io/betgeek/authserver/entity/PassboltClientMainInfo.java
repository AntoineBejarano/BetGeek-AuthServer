package io.betgeek.authserver.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PassboltClientMainInfo {

	private String keyId; 
	private String publicKeyPath;
	private String privateKeyPath;
	private InputStream publicKey;
	private InputStream privateKey;
	private String password;
	private String userIdPassbolt;
	

	private ByteArrayOutputStream baosPublic = null;
	private ByteArrayOutputStream baosPrivate = null;

	private void setBaosPublic() {
		baosPublic = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = this.publicKey.read(buffer)) > -1 ) {
				baosPublic.write(buffer, 0, len);
			}
			baosPublic.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setBaosPrivate() {
		baosPrivate = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = this.privateKey.read(buffer)) > -1 ) {
				baosPrivate.write(buffer, 0, len);
			}
			baosPrivate.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getPublicKeyPath() {
		return publicKeyPath;
	}

	public void setPublicKeyPath(String publicKeyPath) {
		this.publicKeyPath = publicKeyPath;
	}

	public String getPrivateKeyPath() {
		return privateKeyPath;
	}

	public void setPrivateKeyPath(String privateKeyPath) {
		this.privateKeyPath = privateKeyPath;
	}

	public InputStream getPublicKey() {
		return new ByteArrayInputStream(baosPublic.toByteArray());
	}

	public void setPublicKey(InputStream publicKey) {
		this.publicKey = publicKey;
		this.setBaosPublic();
	}

	public InputStream getPrivateKey() {
		return new ByteArrayInputStream(baosPrivate.toByteArray());
	}

	public void setPrivateKey(InputStream privateKey) {
		this.privateKey = privateKey;
		this.setBaosPrivate();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserIdPassbolt() {
		return userIdPassbolt;
	}

	public void setUserIdPassbolt(String userIdPassbolt) {
		this.userIdPassbolt = userIdPassbolt;
	}

}
