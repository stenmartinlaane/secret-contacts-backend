package com.stenmartin.secret_contacts_backend;

import org.springframework.boot.SpringApplication;

public class TestSecretContactsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(SecretContactsBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
