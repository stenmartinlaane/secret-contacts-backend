package com.stenmartin.secret_contacts_backend.persistence.repositories;

import com.stenmartin.secret_contacts_backend.persistence.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
