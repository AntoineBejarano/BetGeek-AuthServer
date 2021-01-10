package io.betgeek.authserver;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer
public class AuthServerApplication {

	private PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}
	
	@PostConstruct
	public void test() {
		passwordEncoder = new BCryptPasswordEncoder();
		System.out.println("INICIADO LUEGO DE ....");
		String password = passwordEncoder.encode("betgeek123");
		System.out.println(password);
	}
}
