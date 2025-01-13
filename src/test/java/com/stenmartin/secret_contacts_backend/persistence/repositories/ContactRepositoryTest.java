package com.stenmartin.secret_contacts_backend.persistence.repositories;

import com.stenmartin.secret_contacts_backend.persistence.entities.Contact;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContactRepositoryTest {

    @Autowired
    private ContactRepository contactRepository;

    @Test
    void saveMethod() {
        final Contact contact = new Contact("Aladin", "123l", "+372 5439348");
        Contact result = contactRepository.save(contact);
        System.out.println("Contact saved: " + result.toString());
    }

}