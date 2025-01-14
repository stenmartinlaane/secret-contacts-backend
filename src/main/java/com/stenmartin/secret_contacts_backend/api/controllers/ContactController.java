package com.stenmartin.secret_contacts_backend.api.controllers;

import com.stenmartin.secret_contacts_backend.api.dto.ContactDto;
import com.stenmartin.secret_contacts_backend.api.dto.Response;
import com.stenmartin.secret_contacts_backend.persistence.entities.Contact;
import com.stenmartin.secret_contacts_backend.services.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContacts());
    }

    @PostMapping
    public ResponseEntity<?> addContact(@RequestBody ContactDto addContactDto) {
        Response<Contact> response = contactService.addContact(addContactDto);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("{id}")
    public ResponseEntity<?> updateContact(@PathVariable Long id, @RequestBody ContactDto contact) {
        Response<Contact> response = contactService.updateContact(contact, id);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<Boolean>> deleteContact(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.deleteContact(id));
    }
}
