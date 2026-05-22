package com.project.auth.auth_backend;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.project.auth.auth_backend.auth.config.AppConstants;
import com.project.auth.auth_backend.auth.entities.Role;
import com.project.auth.auth_backend.auth.repositories.RoleRepository;

@SpringBootApplication
public class AuthBackendApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(AuthBackendApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		// we will create some default user roles
		// ADMIN
		// GUEST

		roleRepository.findByName("ROLE_" + AppConstants.Role_ADMIN).ifPresentOrElse(role -> {
			System.out.println("Admin role already exists");

		}, () -> {

			Role role = new Role();
			role.setName("ROLE_" + AppConstants.Role_ADMIN);
			role.setId(UUID.randomUUID());
			roleRepository.save(role);

		});
		roleRepository.findByName("ROLE_" + AppConstants.Role_GUEST).ifPresentOrElse(role -> {

			System.out.println("Guest role already exists");

		}, () -> {

			Role role = new Role();
			role.setName("ROLE_" + AppConstants.Role_GUEST);
			role.setId(UUID.randomUUID());
			roleRepository.save(role);

		});

	}

}
