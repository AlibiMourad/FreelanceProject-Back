package com.freelance.app.converter;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.freelance.app.dto.PersonDto;
import com.freelance.app.entities.Person;

@Component
public class PersonConvertor implements GenericsConverter<Person, PersonDto> {

	@Override
	public PersonDto entityToDto(Person person) {
		return PersonDto.builder()
				.firstName(person.getFirstName())
				.lastName(person.getLastName())
				.phoneNumber(person.getPhoneNumber())
				.creationDate(LocalDate.now())
				.isActive(true)
				.build();
	}

	@Override
	public Person dtoToEntity(PersonDto persondto) {
		return Person.builder()
				.firstName(persondto.getFirstName())
				.lastName(persondto.getLastName())
				.phoneNumber(persondto.getPhoneNumber())
				.creationDate(LocalDate.now())
				.isActive(true)
				.build();
	}

}