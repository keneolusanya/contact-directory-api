package com.keneolusanya.contact_directory.service;

import com.keneolusanya.contact_directory.model.Contact;
import com.keneolusanya.contact_directory.repository.ContactRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    // once back just start tests again

    public Contact createContact(Contact contact) {
        if(contactRepository.validateEmail(contact.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists: " + contact.getEmail());
        }

        return contactRepository.save(contact);
    }

    public List<Contact> getAllContacts(String group, String search) {
        // filter list here
        // for search, matches first name, last name or email, can use toLowerCase() and contains()
        // check both group and search
        // can use stream
        List<Contact> contacts = contactRepository.findAll();

        if (group != null) {
            contacts = contacts.stream().filter(contact -> contact.getGroup().name().equalsIgnoreCase(group)).toList();
        }

        if (search != null) {
            contacts = contacts.stream()
                    .filter(contact ->
                            contact.getFirstName().toLowerCase().contains(search.toLowerCase())
                                    || contact.getLastName().toLowerCase().contains(search.toLowerCase())
                                    || contact.getEmail().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        }

        return contacts;
    }

    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    public void updateContact(Long id, Contact contact) {
        // check id exists first
        contactRepository.update(id, contact);
    }

    public void deleteContact(Long id) {

        contactRepository.deleteById(id);
    }
}
