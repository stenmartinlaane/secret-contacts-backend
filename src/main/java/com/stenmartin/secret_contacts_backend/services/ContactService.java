package com.stenmartin.secret_contacts_backend.services;

import com.stenmartin.secret_contacts_backend.api.dto.ContactDto;
import com.stenmartin.secret_contacts_backend.api.dto.Response;
import com.stenmartin.secret_contacts_backend.persistence.entities.Contact;
import com.stenmartin.secret_contacts_backend.persistence.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Response<Contact> addContact(ContactDto contactDto) {
        Contact contact = new Contact();
        contact.setName(contactDto.getName());
        contact.setCodeName(contactDto.getCodeName());
        contact.setPhoneNumber(contactDto.getPhoneNumber());
        return new Response<Contact>(contactRepository.save(contact), null, true);
    }

    public Response<Contact> updateContact(ContactDto contactDto, Long id) {
        Optional<Contact> c = contactRepository.findById(id);
        if (c.isEmpty()) {
            return new Response<Contact>(null, "Could not update contact because contact with this id does not exist.", false);
        }
        Contact contact = new Contact();
        contact.setName(contactDto.getName());
        contact.setCodeName(contactDto.getCodeName());
        contact.setPhoneNumber(contactDto.getPhoneNumber());
        contact.setId(id);
        return new Response<Contact>(contactRepository.save(contact), null, true);
    }

    public Response<Boolean> deleteContact(Long id) {
        try {
            contactRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return new Response<Boolean>(false, "Could not delete contact because contact with this id does not exist.", false);
        }
        return new Response<Boolean>(true, null, true);
    }
}
