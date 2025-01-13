package com.stenmartin.secret_contacts_backend.services;

import com.stenmartin.secret_contacts_backend.api.dto.ContactDto;
import com.stenmartin.secret_contacts_backend.api.dto.Response;
import com.stenmartin.secret_contacts_backend.persistence.entities.Contact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContactServiceTest {

    @Autowired
    private ContactService contactService;

    @Test
    void AddContact() {
        final ContactDto contact = new ContactDto("Mike", "lll2", "12312");
        final Response<Contact> result = contactService.addContact(contact);
        assertNotNull(result.getResult().getId());
    }

    @Test
    void DeleteContact() {
        final ContactDto contact = new ContactDto("Anna", "lll2", "12312");
        final Response<Contact> result = contactService.addContact(contact);
        Long id = result.getResult().getId();
        final Response<Boolean> deleteResult = contactService.deleteContact(id);
        assertTrue(deleteResult.isSuccess());
        assertTrue(deleteResult.getResult());
    }

    @Test
    void UpdateContact() {
        final Response<Contact> contact = contactService.addContact(
                new ContactDto("Lina", "lll2", "12312")
        );
        assertTrue(contact.isSuccess());
        final Response<Contact> updateResponse = contactService.updateContact(
                new ContactDto("Lina2", contact.getResult().getCodeName(), contact.getResult().getPhoneNumber()),
                contact.getResult().getId()
        );
        final Contact updatedContact = updateResponse.getResult();
        assertTrue(updateResponse.isSuccess());
        assertEquals("Lina2", updatedContact.getName());
        assertEquals(contact.getResult().getCodeName(), updatedContact.getCodeName());
        assertEquals(contact.getResult().getPhoneNumber(), updatedContact.getPhoneNumber());
    }

    @Test
    void GetAllContacts() {
        int initialSize = contactService.getAllContacts().size();
        contactService.addContact(new ContactDto("Lina", "lll2", "12312"));
        contactService.addContact(new ContactDto("Lina2", "lll2", "12312"));
        Assertions.assertEquals(initialSize + 2, contactService.getAllContacts().size());
    }
}