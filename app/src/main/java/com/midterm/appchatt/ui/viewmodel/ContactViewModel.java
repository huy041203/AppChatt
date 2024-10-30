package com.midterm.appchatt.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.midterm.appchatt.data.repository.ContactRepository;
import com.midterm.appchatt.model.Contact;

import java.util.List;

public class ContactViewModel extends ViewModel {
    private ContactRepository contactRepository;

    public ContactViewModel() {
        contactRepository = new ContactRepository();
    }

    public void addContact(Contact contact) {
        contactRepository.addContact(contact);
    }

    public LiveData<List<Contact>> getContactsForUser(String userId) {
        return contactRepository.getContactsForUser(userId);
    }
    public LiveData<List<Contact>> getContacts() {
        return contactRepository.getContacts();
    }
}
