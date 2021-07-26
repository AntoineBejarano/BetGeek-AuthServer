package io.betgeek.authserver;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

import io.betgeek.domain.configurations.DomainConfiguration;

@SpringBootApplication
@EnableAuthorizationServer
@Import(DomainConfiguration.class)
public class AuthServerApplication {

	private PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}
	
	@PostConstruct
	public void test() {
		passwordEncoder = new BCryptPasswordEncoder();
		System.out.println("INICIADO LUEGO DE ....");
		String password = passwordEncoder.encode("t3mp0r4lp4zzw00rD");
		System.out.println(password);
	}
}
