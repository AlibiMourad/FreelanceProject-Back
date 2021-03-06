package com.freelance.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.freelance.app.entities.CompanyClient;

@RepositoryRestResource
public interface CompanyClientRepository extends JpaRepository<CompanyClient, Long> {
	
	CompanyClient findByEmailContact(String email);

}
