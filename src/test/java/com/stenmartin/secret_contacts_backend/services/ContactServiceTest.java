package com.stenmartin.secret_contacts_backend.services;

import com.stenmartin.secret_contacts_backend.api.dto.ContactDto;
import com.stenmartin.secret_contacts_backend.api.dto.Response;
import com.stenmartin.secret_contacts_backend.persistence.entities.Contact;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Testcontainers
class ContactServiceTest {

    @Autowired
    private ContactService contactService;

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:16");

    @Test
    void connectionTest() {
        assertTrue(postgreSQLContainer.isCreated());
        assertTrue(postgreSQLContainer.isRunning());
    }


    @Test
    void AddContact() {
        final ContactDto contact = new ContactDto("Mike", "lll2", "12312");
        final Response<Contact> result = contactService.addContact(contact);
        assertNotNull(result.getResult().getId(), "Id should be added to the contact");
        Assertions.assertEquals(1, contactService.getAllContacts().size() ,"one contact should be added");
    }

    @Test
    void DeleteContact() {
        final ContactDto contact = new ContactDto("Anna", "lll2", "12312");
        final Response<Contact> result = contactService.addContact(contact);
        assertTrue(result.isSuccess(), "Adding contact should be successful");
        assertNotNull(result.getResult());
        Long id = result.getResult().getId();
        final Response<Boolean> deleteResult = contactService.deleteContact(id);
        assertTrue(deleteResult.isSuccess(), "Contact should be deleted successfully");
        assertTrue(deleteResult.getResult(), "The result should be true");
        Assertions.assertEquals(0, contactService.getAllContacts().size(), "Nothing should remain in database");
    }

    @Test
    void UpdateContact() {
        final Response<Contact> contact = contactService.addContact(
                new ContactDto("Lina", "lll2", "12312")
        );
        assertTrue(contact.isSuccess(), "Contact should be added successfully");
        final Response<Contact> updateResponse = contactService.updateContact(
                new ContactDto("Lina2", contact.getResult().getCodeName(), contact.getResult().getPhoneNumber()),
                contact.getResult().getId()
        );
        final Contact updatedContact = updateResponse.getResult();
        assertTrue(updateResponse.isSuccess(), "Contact should be updated successfully");
        assertEquals("Lina2", updatedContact.getName(), "Name should be updated");
        assertEquals(contact.getResult().getCodeName(), updatedContact.getCodeName(), "Code name should remain the same");
        assertEquals(contact.getResult().getPhoneNumber(), updatedContact.getPhoneNumber(), "Phone number should remain the same");
    }

    @Test
    void GetAllContacts() {
        contactService.addContact(new ContactDto("Lina", "lll2", "12312"));
        contactService.addContact(new ContactDto("Lina2", "lll2", "12312"));
        Assertions.assertEquals(2, contactService.getAllContacts().size());
    }
}