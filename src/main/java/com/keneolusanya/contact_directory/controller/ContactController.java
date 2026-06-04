package com.keneolusanya.contact_directory.controller;

import com.keneolusanya.contact_directory.model.Contact;
import com.keneolusanya.contact_directory.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<Contact> postContact(@Valid @RequestBody Contact contact) {
       return ResponseEntity.status(HttpStatus.CREATED).body(contactService.createContact(contact));
    }

    @GetMapping
    public List<Contact> getContacts(@RequestParam(required = false) String group, @RequestParam(required = false) String search) {
        return contactService.getAllContacts(group, search);
    }

    // wraps the Contact with the response code incase not valid or similar
    // errors out as necessary
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id){
        return contactService.getContactById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public void putContactById(@PathVariable Long id,@Valid @RequestBody Contact contact) {
        contactService.updateContact(id, contact);
    }

    @DeleteMapping("/{id}")
    public void deleteContactByid(@PathVariable Long id) {
        contactService.deleteContact(id);
    }



}
