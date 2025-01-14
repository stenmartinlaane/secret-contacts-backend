package com.stenmartin.secret_contacts_backend.api.controllers;

import com.stenmartin.secret_contacts_backend.api.dto.ContactDto;
import com.stenmartin.secret_contacts_backend.api.dto.Response;
import com.stenmartin.secret_contacts_backend.persistence.entities.Contact;
import com.stenmartin.secret_contacts_backend.persistence.repositories.ContactRepository;
import com.stenmartin.secret_contacts_backend.services.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
class ContactControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactRepository contactRepository;

    @BeforeEach
    public void setUp() {
        contactRepository.deleteAll();
        List<ContactDto> contacts = List.of(
                new ContactDto("Anna", "4nn4", "+372 3422930"),
                new ContactDto("Peter", "PPP", "+372 645345"),
                new ContactDto("Julia", "hdks", "+372 9993294"),
                new ContactDto("Alex", "ao4", "+372 7193420"),
                new ContactDto("Mike", "mm4", "+372 8930452")
                );

        for (ContactDto contact : contacts) {
            contactService.addContact(contact);
        }
    }

    @Test
    void connectionTest() {
        assertTrue(postgreSQLContainer.isCreated());
        assertTrue(postgreSQLContainer.isRunning());
    }

    @Test
    void shouldFindAllContacts() {
        Contact[] contacts = restTemplate.getForObject("/contacts", Contact[].class);
        assertEquals(5, contacts.length);
    }

    @Test
    void shouldAddContact() {
        ContactDto newContact = new ContactDto("John", "john123", "+372 1234567");
        ResponseEntity<Response<Contact>> responseEntity = restTemplate.exchange(
                "/contacts",
                HttpMethod.POST,
                new HttpEntity<>(newContact),
                new ParameterizedTypeReference<Response<Contact>>() {}
        );
        Response<Contact> response = responseEntity.getBody();
        assertNotNull(response);
        assertTrue(response.isSuccess(), "Operation should be successful");
        Contact addedContact = response.getResult();
        Contact[] contacts = restTemplate.getForObject("/contacts", Contact[].class);
        Contact contact = Arrays.stream(contacts)
                .filter(c -> c.getId().equals(addedContact.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(contact);
        assertEquals(6, contacts.length, "The contact should be added");
        assertEquals(newContact.getName(), contact.getName(), "Name should remain the same");
        assertEquals(newContact.getCodeName(), contact.getCodeName(), "Code name should remain the same");
        assertEquals(newContact.getPhoneNumber(), contact.getPhoneNumber(), "Phone number should remain the same");
    }

    @Test
    void shouldRemoveContact() {
        Contact[] c = restTemplate.getForObject("/contacts", Contact[].class);
        restTemplate.delete("/contacts/{id}", c[0].getId());
        Contact[] contacts = restTemplate.getForObject("/contacts", Contact[].class);
        assertEquals(4, contacts.length, "The contact should be removed");
    }

    @Test
    void shouldUpdateContact() {
        Contact[] contactsBeforeUpdate = restTemplate.getForObject("/contacts", Contact[].class);
        assertNotNull(contactsBeforeUpdate);
        assertEquals(5, contactsBeforeUpdate.length);

        Contact contactToUpdate = contactsBeforeUpdate[0];
        ContactDto updatedContactDto = new ContactDto(
                contactToUpdate.getName() + "AAA",
                contactToUpdate.getCodeName() + "AAA",
                contactToUpdate.getPhoneNumber() + "AAA"
        );

        ResponseEntity<Response<Contact>> responseEntity = restTemplate.exchange(
                "/contacts/" + contactToUpdate.getId(),
                HttpMethod.POST,
                new HttpEntity<>(updatedContactDto),
                new ParameterizedTypeReference<Response<Contact>>() {}
        );

        Response<Contact> response = responseEntity.getBody();
        assertNotNull(response);
        assertTrue(response.isSuccess());

        Contact updatedContact = response.getResult();
        assertNotNull(updatedContact);
        assertEquals(contactToUpdate.getId(), updatedContact.getId(), "Id should not change");

        Contact[] contactsAfterUpdate = restTemplate.getForObject("/contacts", Contact[].class);
        assertNotNull(contactsAfterUpdate);
        assertEquals(contactsBeforeUpdate.length, contactsAfterUpdate.length, "The contacts list should not change in size");

        Contact contactInList = Arrays.stream(contactsAfterUpdate)
                .filter(c -> c.getId().equals(updatedContact.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(contactInList, "The updated contact should be present in the contact list");

        assertEquals(updatedContactDto.getName(), contactInList.getName(), "Name should be updated correctly");
        assertEquals(updatedContactDto.getCodeName(), contactInList.getCodeName(), "Code name should be updated correctly");
        assertEquals(updatedContactDto.getPhoneNumber(), contactInList.getPhoneNumber(), "Phone number should be updated correctly");
    }
}