package com.keneolusanya.contact_directory.repository;

import com.keneolusanya.contact_directory.model.Contact;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ContactRepository {

    // ConcurrentMap and AtomicLong for hypothetical thread safety
    private final ConcurrentHashMap<Long, Contact> contacts = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(0);

    // default constructor

    public Contact save(Contact contact) {
        Long id = counter.incrementAndGet();

        contact.setId(id);
        contact.setCreatedAt(LocalDateTime.now());
        contacts.put(id, contact);

        return contact;
    }

    public void update(Long id, Contact contact) {
        if (contacts.containsKey(id)) {
            contacts.replace(id, contact);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found with Id: " + id);
        }
    }

    public List<Contact> findAll() {
        return new ArrayList<>(contacts.values());
    }

    // null allowed
    public Optional<Contact> findById(Long id) {
        return Optional.ofNullable(contacts.get(id));
    }

    public void deleteById(Long id) {
        if (contacts.containsKey(id)) {
            contacts.remove(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found with Id: " + id);
        }

    }

    public boolean validateEmail(String email) {
        List<Contact> contactList = new ArrayList<>(contacts.values());

        for (Contact contact : contactList) {
            if (contact.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }

        return false;
    }
}
